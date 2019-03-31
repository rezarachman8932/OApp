package com.app.o.user.front

import com.app.o.api.APIRepository
import com.app.o.api.login.account.LoginResponse
import com.app.o.api.login.third_party.LoginSocialMediaSpec
import com.app.o.base.presenter.OAppPresenter
import com.app.o.base.service.OAppViewService
import com.app.o.shared.util.OAppUtil
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

class UserRegisterLoginPresenter(private val view: OAppViewService<LoginResponse>,
                                 private val compositeDisposable: CompositeDisposable) : OAppPresenter() {

    fun doLoginWithThirdParty(email: String?, name:String?, password: String, loginType: String) {
        val spec = LoginSocialMediaSpec(email, name, password, loginType, getFCMToken())

        compositeDisposable.add(APIRepository.create().loginWithThirdParty(spec)
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

}