package com.app.o.home

import com.app.o.api.APIRepository
import com.app.o.api.home.HomeResponse
import com.app.o.api.home.HomeResponseZip
import com.app.o.api.location.LocationSpec
import com.app.o.api.location.LocationWithQuerySpec
import com.app.o.api.relation.UserConnectedCountResponse
import com.app.o.api.relation.UserConnectedResponse
import com.app.o.base.presenter.OAppPresenter
import com.app.o.base.service.OAppSearchService
import com.app.o.base.service.OAppViewService
import com.app.o.shared.OAppUtil
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.functions.Function3
import io.reactivex.schedulers.Schedulers

class HomePresenter(
        private val view: OAppViewService<HomeResponseZip>,
        private val viewSearchService: OAppSearchService,
        private val compositeDisposable: CompositeDisposable) : OAppPresenter() {

    fun getPostedTimeline(spec: LocationSpec) {
        compositeDisposable.add(getHomeContent(spec, getHeaderAuth())
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

    fun getSearchPost(spec: LocationWithQuerySpec) {
        compositeDisposable.add(APIRepository.create().postSearch(spec, getHeaderAuth())
                .subscribeOn(Schedulers.io())
                .compose {
                    it.observeOn(AndroidSchedulers.mainThread())
                }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    viewSearchService.onQueryProcessed()
                }
                .doOnError{
                    viewSearchService.onQueryFailed()
                    it.printStackTrace()
                }
                .subscribe(Consumer {
                    viewSearchService.onQueryCompleted(it)
                }))
    }

    private fun getHomeContent(locationSpec: LocationSpec, token: String) : Single<HomeResponseZip> {
        return Single.zip(
                APIRepository.create().post(locationSpec, token),
                APIRepository.create().getPeopleConnected(locationSpec, token),
                APIRepository.create().getPeopleConnectedCount(locationSpec, token),
                Function3<HomeResponse, UserConnectedResponse, UserConnectedCountResponse, HomeResponseZip> {
                    t1, t2, t3 ->
                    createDetailModel(t1, t2, t3)
                })
    }

    private fun createDetailModel(homeResponse: HomeResponse, userConnectedResponse: UserConnectedResponse, userConnectedCountResponse: UserConnectedCountResponse) : HomeResponseZip {
        return HomeResponseZip(homeResponse, userConnectedResponse, userConnectedCountResponse)
    }

}