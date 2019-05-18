package com.app.o.user.connected

import com.app.o.api.APIRepository
import com.app.o.api.location.LocationSpec
import com.app.o.api.relation.UserConnectedResponse
import com.app.o.base.presenter.OAppPresenter
import com.app.o.base.service.OAppViewService
import com.app.o.shared.util.OAppUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class ConnectedUsersPresenter(private val view: OAppViewService<UserConnectedResponse>,
                              private val compositeDisposable: CompositeDisposable) : OAppPresenter() {

    fun getConnectedUsers(locationSpec: LocationSpec) {
        try {
            compositeDisposable.add(APIRepository.create().getPeopleConnected(locationSpec, getHeaderAuth())
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
                    .subscribe { userConnectedResponse, throwable ->
                        val succeed = {
                            view.onDataResponse(userConnectedResponse)
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

}