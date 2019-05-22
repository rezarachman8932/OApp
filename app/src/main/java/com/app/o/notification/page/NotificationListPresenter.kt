package com.app.o.notification.page

import com.app.o.api.APIRepository
import com.app.o.api.notification.PushNotificationReadSpec
import com.app.o.api.notification.PushNotificationResponse
import com.app.o.base.presenter.OAppPresenter
import com.app.o.base.service.OAppViewService
import com.app.o.shared.util.OAppUtil
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

class NotificationListPresenter(private val view: OAppViewService<PushNotificationResponse>,
                                private val notificationListCallback: NotificationListCallback,
                                private val compositeDisposable: CompositeDisposable) : OAppPresenter() {

    fun getPushNotificationList() {
        try {
            compositeDisposable.add(APIRepository.create().getPushNotificationList(getHeaderAuth())
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

    fun setPushNotificationAsRead(spec: PushNotificationReadSpec) {
        try {
            compositeDisposable.add(APIRepository.create().setPushNotificationAsRead(spec, getHeaderAuth())
                    .subscribeOn(Schedulers.io())
                    .compose {
                        it.observeOn(AndroidSchedulers.mainThread())
                    }
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe {
                        notificationListCallback.onSetAsReadProgress()
                    }
                    .onErrorResumeNext {
                        notificationListCallback.onSetAsReadComplete(OAppUtil.ON_FINISH_FAILED)
                        Single.error(it)
                    }
                    .doOnError{
                        notificationListCallback.onSetAsReadComplete(OAppUtil.ON_FINISH_FAILED)
                        it.printStackTrace()
                    }
                    .subscribe(Consumer {
                        notificationListCallback.onSetAsReadComplete(OAppUtil.ON_FINISH_SUCCEED)
                    })
            )
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }

}