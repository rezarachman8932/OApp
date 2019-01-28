package com.app.o.user.detail

import com.app.o.api.APIRepository
import com.app.o.api.home.HomeResponse
import com.app.o.api.user.UserProfileResponse
import com.app.o.api.user.UserProfileResponseZip
import com.app.o.api.user.UserProfileSpec
import com.app.o.base.presenter.OAppPresenter
import com.app.o.base.service.OAppViewService
import com.app.o.shared.OAppUtil
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

class UserProfilePresenter(private val view: OAppViewService<UserProfileResponseZip>,
                           private val compositeDisposable: CompositeDisposable) : OAppPresenter() {

    fun getProfile(spec: UserProfileSpec?) {
        compositeDisposable.add(getAllContent(spec)
                .subscribeOn(Schedulers.io())
                .compose {
                    it.observeOn(AndroidSchedulers.mainThread())
                }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    view.showLoading()
                }
                .doOnError{
                    view.hideLoading(OAppUtil.ON_FINISH_FAILED)
                    it.printStackTrace()
                }
                .subscribe(Consumer {
                    view.onDataResponse(it)
                    view.hideLoading(OAppUtil.ON_FINISH_SUCCEED)
                }))
    }

    private fun getAllContent(spec: UserProfileSpec?) : Single<UserProfileResponseZip> {
        return Single.zip(
                getUserDetailContent(spec),
                APIRepository.create().getUserPostedItems(spec, getHeaderAuth()),
                BiFunction<UserProfileResponse, HomeResponse, UserProfileResponseZip> {
                    t1, t2 -> UserProfileResponseZip(t1, t2)
                })
    }

    private fun getUserDetailContent(spec: UserProfileSpec?) : Single<UserProfileResponse> {
        return if (spec != null) {
            APIRepository.create().getUserProfile(spec, getHeaderAuth())
        } else {
            APIRepository.create().getOwnProfile(getHeaderAuth())
        }
    }

}