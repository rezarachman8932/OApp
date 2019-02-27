package com.app.o.user.login

import com.app.o.api.APIRepository
import com.app.o.api.login.LoginResponse
import com.app.o.api.login.LoginSpec
import com.app.o.base.presenter.OAppPresenter
import com.app.o.base.service.OAppViewService
import com.app.o.shared.util.OAppUtil
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

class LoginPresenter(private val view: OAppViewService<LoginResponse>,
                     private val callback: LoginCallback,
                     private val compositeDisposable: CompositeDisposable) : OAppPresenter() {

    private fun doLogin(loginSpec: LoginSpec) {
        compositeDisposable.add(APIRepository.create().login(loginSpec, getToken())
                .subscribeOn(Schedulers.io())
                .compose {
                    it.observeOn(AndroidSchedulers.mainThread())
                }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    view.showLoading()
                }
                .onErrorResumeNext {
                    view.hideLoading(OAppUtil.ON_FINISH_FAILED)
                    Single.error(it)
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

    fun validateLogin(username: String, password: String) {
        if (username.isEmpty() || password.isEmpty()) {
            callback.onEmptyInput()
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

        val loginSpec = LoginSpec(username, password, getFCMToken())
        doLogin(loginSpec)
    }

}