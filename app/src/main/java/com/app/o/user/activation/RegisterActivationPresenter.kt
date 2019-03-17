package com.app.o.user.activation

import com.app.o.api.APIRepository
import com.app.o.api.activation.ActivationTokenResponse
import com.app.o.api.activation.ActivationTokenSpec
import com.app.o.base.presenter.OAppPresenter
import com.app.o.base.service.OAppViewService
import com.app.o.shared.util.OAppUtil
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

class RegisterActivationPresenter(private val view: OAppViewService<ActivationTokenResponse>,
                                  private val compositeDisposable: CompositeDisposable) : OAppPresenter() {

    fun activateAccount(activationTokenSpec: ActivationTokenSpec) {
        compositeDisposable.add(APIRepository.create().activateUserToken(activationTokenSpec, getHeaderAuth())
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