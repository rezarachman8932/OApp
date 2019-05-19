package com.app.o.user.login

import com.app.o.api.APIRepository
import com.app.o.api.login.account.LoginResponse
import com.app.o.api.login.account.LoginSpec
import com.app.o.base.presenter.OAppPresenter
import com.app.o.base.service.OAppViewService
import com.app.o.shared.util.OAppUtil
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeoutException

class LoginPresenter(private val view: OAppViewService<LoginResponse>,
                     private val callback: LoginCallback,
                     private val compositeDisposable: CompositeDisposable) : OAppPresenter() {

    private fun doLogin(loginSpec: LoginSpec) {
        try {
            compositeDisposable.add(APIRepository.create().login(loginSpec)
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
                    .subscribe { loginResponse, throwable ->
                        val succeed = {
                            view.onDataResponse(loginResponse)
                            view.hideLoading(OAppUtil.ON_FINISH_SUCCEED)
                        }

                        val failed = {
                            view.hideLoading(OAppUtil.ON_FINISH_FAILED)
                        }

                        subscriberHandler(throwable, succeed, failed)
                    }
            )
        } catch (illegalException: IllegalStateException) {
            illegalException.printStackTrace()
        } catch (timeoutException: TimeoutException) {
            timeoutException.printStackTrace()
        }
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