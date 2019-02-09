package com.app.o.user.update_profile

import com.app.o.api.APIRepository
import com.app.o.api.user.UserProfileSpec
import com.app.o.api.user.UserUpdateProfileResponse
import com.app.o.api.user.update.UserUpdateProfileSpec
import com.app.o.base.presenter.OAppPresenter
import com.app.o.base.service.OAppViewService
import com.app.o.shared.util.OAppUserUtil
import com.app.o.shared.util.OAppUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

class UpdateProfilePresenter(private val view: OAppViewService<UserUpdateProfileResponse>,
                             private val callback: UpdateProfileCallback,
                             private val compositeDisposable: CompositeDisposable) : OAppPresenter() {

    private fun updateProfile(spec: UserUpdateProfileSpec) {
        compositeDisposable.add(APIRepository.create().updateProfile(spec, getHeaderAuth())
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

    fun getCurrentProfile() {
        compositeDisposable.add(APIRepository.create().getUserProfile(UserProfileSpec(OAppUserUtil.getUserId()), getHeaderAuth())
                .subscribeOn(Schedulers.io())
                .compose {
                    it.observeOn(AndroidSchedulers.mainThread())
                }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    callback.onLoadCurrentProfile()
                }
                .doOnError{
                    callback.onFailedGetCurrentProfile()
                    it.printStackTrace()
                }
                .subscribe(Consumer {
                    callback.onSucceedGetCurrentProfile(it)
                }))
    }

    fun validateProfileUpdated(phoneNumber: String, location: String?, facebook: String?, twitter: String?, instagram: String?) {
        if (phoneNumber.length < OAppUtil.MINIMUM_PHONE_CHARS) {
            callback.onPhoneNumberNotComplete()
            return
        }

        if (location.isNullOrEmpty()) {
            callback.onLocationNotComplete()
            return
        }

        val updateProfileSpec = UserUpdateProfileSpec(
                phoneNumber,
                location,
                "oke.com",
                facebook,
                twitter,
                instagram,
                "test"
        )
        updateProfile(updateProfileSpec)
    }

}