package com.app.o.user.register

import com.app.o.api.APIRepository
import com.app.o.api.register.RegisterResponse
import com.app.o.api.register.RegisterSpec
import com.app.o.base.service.OAppViewService
import com.app.o.shared.OAppUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

class RegisterPresenter(private val view: OAppViewService<RegisterResponse>,
                        private val callback: RegisterCallback,
                        private val compositeDisposable: CompositeDisposable) {

    private fun doSignUp(registerSpec: RegisterSpec) {
        compositeDisposable.add(APIRepository.create().register(registerSpec)
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

    fun validateSignUp(name: String, phoneNumber: String, email: String, username: String, password: String) {
        if (name.isEmpty() || phoneNumber.isEmpty() || email.isEmpty() || username.isEmpty() || password.isEmpty()) {
            callback.onAllInputEmpty()
            return
        }

        if (name.length < OAppUtil.MINIMUM_CHARS) {
            callback.onNameNotComplete()
            return
        }

        if (phoneNumber.length < OAppUtil.MINIMUM_PHONE_CHARS) {
            callback.onPhoneNumberNotComplete()
            return
        }

        if (!OAppUtil.isValidEmail(email)) {
            callback.onEmailNotValid()
            return
        }

        if (username.length < OAppUtil.MINIMUM_CHARS) {
            callback.onUsernameNotComplete()
            return
        }

        if (password.length < OAppUtil.MINIMUM_CHARS) {
            callback.onPasswordNotComplete()
            return
        }

        val registerSpec = RegisterSpec(name, phoneNumber, email, username, password)
        doSignUp(registerSpec)
    }

}