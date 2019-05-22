package com.app.o.user.blocked

import com.app.o.api.APIRepository
import com.app.o.api.user.blocked.BlockedUserResponse
import com.app.o.api.user.blocked.UserBlockingSpec
import com.app.o.base.presenter.OAppPresenter
import com.app.o.base.service.OAppViewService
import com.app.o.shared.util.OAppUtil
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

class BlockedAccountPresenter(private val view: OAppViewService<BlockedUserResponse>,
                              private val unBlockCallback: UnblockedAccountCallback,
                              private val compositeDisposable: CompositeDisposable) : OAppPresenter() {

    fun getBlockedUsers() {
        try {
            compositeDisposable.add(APIRepository.create().getBlockedUsers(getHeaderAuth())
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
                    })
            )
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }

    fun unBlockedUser(spec: UserBlockingSpec) {
        try {
            compositeDisposable.add(APIRepository.create().unBlockedUser(spec, getHeaderAuth())
                    .subscribeOn(Schedulers.io())
                    .compose {
                        it.observeOn(AndroidSchedulers.mainThread())
                    }
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe {
                        unBlockCallback.onProgress()
                    }
                    .onErrorResumeNext {
                        unBlockCallback.onFailed()
                        Single.error(it)
                    }
                    .doOnError{
                        unBlockCallback.onFailed()
                        it.printStackTrace()
                    }
                    .subscribe(Consumer {
                        unBlockCallback.onSucceed(it)
                    })
            )
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }

}