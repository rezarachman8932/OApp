package com.app.o.user.detail

import com.app.o.api.APIRepository
import com.app.o.api.home.HomeResponse
import com.app.o.api.user.profile.UserProfileResponse
import com.app.o.api.user.profile.UserProfileResponseZip
import com.app.o.api.user.profile.UserProfileSpec
import com.app.o.base.presenter.OAppPresenter
import com.app.o.base.service.OAppViewService
import com.app.o.shared.util.OAppUtil
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers

class UserProfilePresenter(private val view: OAppViewService<UserProfileResponseZip>,
                           private val compositeDisposable: CompositeDisposable) : OAppPresenter() {

    fun getProfile(userId: Int) {
        try {
            compositeDisposable.add(getAllContent(userId)
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
                    .subscribe { userProfileResponseZip, throwable ->
                        val succeed = {
                            view.onDataResponse(userProfileResponseZip)
                            view.hideLoading(OAppUtil.ON_FINISH_SUCCEED)
                        }

                        val failed = {
                            view.hideLoading(OAppUtil.ON_FINISH_FAILED)
                        }

                        subscriberHandler(throwable, succeed, failed)
                    }
            )
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }

    private fun getAllContent(userId: Int) : Single<UserProfileResponseZip> {
        return Single.zip(
                getUserDetailContent(userId),
                getPostedItems(userId),
                BiFunction<UserProfileResponse, HomeResponse, UserProfileResponseZip> {
                    t1, t2 ->
                    UserProfileResponseZip(t1, t2)
                })
    }

    private fun getPostedItems(userId: Int) : Single<HomeResponse> {
        return if (userId > 0) {
            APIRepository.create().getUserPostedItems(UserProfileSpec(userId), getHeaderAuth())
        } else {
            APIRepository.create().getOwnPostedItems(getHeaderAuth())
        }
    }

    private fun getUserDetailContent(userId: Int) : Single<UserProfileResponse> {
        return if (userId > 0) {
            APIRepository.create().getUserProfile(UserProfileSpec(userId), getHeaderAuth())
        } else {
            APIRepository.create().getOwnProfile(getHeaderAuth())
        }
    }

}