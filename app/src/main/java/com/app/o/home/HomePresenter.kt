package com.app.o.home

import com.app.o.api.APIRepository
import com.app.o.api.home.HomePostItem
import com.app.o.base.service.OAppViewService
import com.app.o.shared.OAppUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

class HomePresenter(
        private val view: OAppViewService<List<HomePostItem>>,
        private val compositeDisposable: CompositeDisposable) {

    fun getPostedTimeline(longitude: String, latitude: String) {
        compositeDisposable.add(APIRepository.create().post(longitude, latitude, getJWTToken(OAppUtil.getUserName(), OAppUtil.getToken()))
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

    fun saveLastLocation(longitude: String, latitude: String) {
        OAppUtil.setLongitude(longitude)
        OAppUtil.setLatitude(latitude)
    }

    private fun getJWTToken(username: String?, token: String?): String {
        return OAppUtil.generateJWTToken(username, token)
    }

}