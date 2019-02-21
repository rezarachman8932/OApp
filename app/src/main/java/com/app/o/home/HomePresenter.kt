package com.app.o.home

import com.app.o.api.APIRepository
import com.app.o.api.home.HomeResponse
import com.app.o.api.home.HomeResponseZip
import com.app.o.api.location.LocationSpec
import com.app.o.api.location.LocationWithQuerySpec
import com.app.o.api.relation.UserConnectedCountResponse
import com.app.o.base.presenter.OAppPresenter
import com.app.o.base.service.OAppSearchService
import com.app.o.base.service.OAppViewService
import com.app.o.shared.util.OAppUtil
import com.app.o.user.logout.LogoutCallback
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

class HomePresenter(
        private val view: OAppViewService<HomeResponseZip>,
        private val viewSearchService: OAppSearchService,
        private val logoutCallback: LogoutCallback,
        private val compositeDisposable: CompositeDisposable) : OAppPresenter() {

    fun logout() {
        compositeDisposable.add(APIRepository.create().logout(getHeaderAuth())
                .subscribeOn(Schedulers.io())
                .compose {
                    it.observeOn(AndroidSchedulers.mainThread())
                }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    logoutCallback.onLogoutProceed()
                }
                .onErrorResumeNext {
                    logoutCallback.onLogoutFailed()
                    Single.error(it)
                }
                .doOnError{
                    logoutCallback.onLogoutFailed()
                    it.printStackTrace()
                }
                .subscribe(Consumer {
                    logoutCallback.onLogoutSucceed()
                }))
    }

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
                .onErrorResumeNext {
                    viewSearchService.onQueryFailed()
                    Single.error(it)
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
                APIRepository.create().getPeopleConnectedCount(locationSpec, token),
                BiFunction<HomeResponse, UserConnectedCountResponse, HomeResponseZip> {
                    t1, t2 ->
                    createDetailModel(t1, t2)
                })
    }

    private fun createDetailModel(homeResponse: HomeResponse, userConnectedCountResponse: UserConnectedCountResponse) : HomeResponseZip {
        return HomeResponseZip(homeResponse, userConnectedCountResponse)
    }

}