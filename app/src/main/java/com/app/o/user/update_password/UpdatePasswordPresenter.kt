package com.app.o.user.update_password

import com.app.o.api.APIRepository
import com.app.o.api.user.update.password.UpdatePasswordResponse
import com.app.o.api.user.update.password.UpdatePasswordSpec
import com.app.o.base.presenter.OAppPresenter
import com.app.o.base.service.OAppViewService
import com.app.o.shared.util.OAppUtil
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

class UpdatePasswordPresenter(private val view: OAppViewService<UpdatePasswordResponse>,
                              private val callback: UpdatePasswordCallback,
                              private val compositeDisposable: CompositeDisposable) : OAppPresenter() {

    private fun updateUserPassword(spec: UpdatePasswordSpec) {
        compositeDisposable.add(APIRepository.create().updatePassword(spec, getHeaderAuth())
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

    fun validatePasswordChanged(current: String, updated: String, retypeUpdated: String) {
        if (current.isEmpty() || updated.isEmpty() || retypeUpdated.isEmpty()) {
            callback.onErrorAllFieldsRequired()
            return
        }

        if (current.equals(updated, false)) {
            callback.onErrorCurrentPasswordEqualsNewPassword()
            return
        }

        if (!updated.equals(retypeUpdated, false)) {
            callback.onErrorNotMatchedRetype()
            return
        }

        if (updated.length < OAppUtil.MINIMUM_CHARS) {
            callback.onErrorPasswordLessThanMinChars()
            return
        }

        updateUserPassword(UpdatePasswordSpec(current, updated))
    }

}