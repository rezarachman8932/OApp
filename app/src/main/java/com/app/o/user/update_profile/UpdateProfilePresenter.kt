package com.app.o.user.update_profile

import com.app.o.api.APIRepository
import com.app.o.api.user.profile.UserProfileSpec
import com.app.o.api.user.update.profile.UserUpdateProfileResponse
import com.app.o.api.user.update.profile.UserUpdateProfileSpec
import com.app.o.base.presenter.OAppPresenter
import com.app.o.base.service.OAppViewService
import com.app.o.shared.util.OAppUserUtil
import com.app.o.shared.util.OAppUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody

class UpdateProfilePresenter(private val view: OAppViewService<UserUpdateProfileResponse>,
                             private val callback: UpdateProfileCallback,
                             private val pickImageCallback: UpdateProfileAvatarCallback,
                             private val compositeDisposable: CompositeDisposable) : OAppPresenter() {

    private fun updateProfile(spec: UserUpdateProfileSpec) {
        try {
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
                    .subscribe { userUpdateProfileResponse, throwable ->
                        val succeed = {
                            view.onDataResponse(userUpdateProfileResponse)
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

    fun updateAvatar(file: MultipartBody.Part) {
        try {
            compositeDisposable.add(APIRepository.create().updateAvatar(file, getHeaderAuth())
                    .subscribeOn(Schedulers.io())
                    .compose {
                        it.observeOn(AndroidSchedulers.mainThread())
                    }
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe {
                        pickImageCallback.onLoadingImage()
                    }
                    .doOnError{
                        pickImageCallback.onFailedGettingImage()
                        it.printStackTrace()
                    }
                    .subscribe { updateAvatarResponse, throwable ->
                        val succeed = { pickImageCallback.onSucceedGettingImage(updateAvatarResponse) }
                        val failed = { pickImageCallback.onFailedGettingImage() }

                        subscriberHandler(throwable, succeed, failed)
                    }
            )
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }

    fun getCurrentProfile() {
        try {
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
                    .subscribe { userProfileResponse, throwable ->
                        val succeed = { callback.onSucceedGetCurrentProfile(userProfileResponse) }
                        val failed = { callback.onFailedGetCurrentProfile() }

                        subscriberHandler(throwable, succeed, failed)
                    }
            )
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
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