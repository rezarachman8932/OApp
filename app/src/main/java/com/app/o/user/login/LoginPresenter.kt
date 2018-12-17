package com.app.o.user.login

import com.app.o.api.APIRepository
import com.app.o.api.login.LoginResponse
import com.app.o.api.login.LoginSpec
import com.app.o.base.service.OAppViewService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

class LoginPresenter(private val view: OAppViewService<LoginResponse>,
                     private val callback: LoginCallback,
                     private val compositeDisposable: CompositeDisposable) {

    private fun doLogin(loginSpec: LoginSpec) {
        compositeDisposable.add(APIRepository.create().login(loginSpec)
                .subscribeOn(Schedulers.io())
                .compose {
                    it.observeOn(AndroidSchedulers.mainThread())
                }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    view.showLoading()
                }
                .doOnError{
                    view.hideLoading()
                }
                .subscribe(Consumer {
                    view.onDataResponse(it)
                    view.hideLoading()
                }))
    }

    fun validateLogin(username: String, password: String) {
        if (username.isEmpty() || password.isEmpty()) {
            callback.onEmptyInput()
            return
        }

        if (username.length < 6) {
            callback.onUsernameNotComplete()
            return
        }

        if (password.length < 6) {
            callback.onPasswordNotComplete()
            return
        }

        val loginSpec = LoginSpec(username, password)
        doLogin(loginSpec)
    }

}