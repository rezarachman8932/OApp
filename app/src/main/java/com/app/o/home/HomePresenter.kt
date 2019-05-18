package com.app.o.home

import com.app.o.api.APIRepository
import com.app.o.api.home.HomeResponse
import com.app.o.api.home.HomeResponseZip
import com.app.o.api.location.LocationSpec
import com.app.o.api.location.LocationWithQuerySpec
import com.app.o.api.notification.PushNotificationResponse
import com.app.o.api.relation.UserConnectedCountResponse
import com.app.o.api.user.profile.UserProfileResponse
import com.app.o.api.user.profile.UserProfileSpec
import com.app.o.base.presenter.OAppPresenter
import com.app.o.base.service.OAppSearchService
import com.app.o.base.service.OAppViewService
import com.app.o.shared.util.OAppUserUtil
import com.app.o.shared.util.OAppUtil
import com.app.o.user.logout.LogoutCallback
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Function4
import io.reactivex.schedulers.Schedulers

class HomePresenter(
        private val view: OAppViewService<HomeResponseZip>,
        private val viewSearchService: OAppSearchService,
        private val logoutCallback: LogoutCallback,
        private val compositeDisposable: CompositeDisposable) : OAppPresenter() {

    fun logout() {
        try {
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
                    .subscribe { _, throwable ->
                        val logoutSucceed: ()->Unit = { logoutCallback.onLogoutSucceed() }
                        val logoutFailed: ()->Unit = { logoutCallback.onLogoutFailed() }

                        subscriberHandler(throwable, logoutSucceed, logoutFailed)
                    })
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }

    fun getPostedTimeline(spec: LocationSpec) {
        try {
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
                    .subscribe { homeResponseZip, throwable ->
                        val successUnit: ()->Unit = {
                            view.onDataResponse(homeResponseZip)
                            view.hideLoading(OAppUtil.ON_FINISH_SUCCEED)
                        }

                        val errorUnit: ()->Unit = {
                            view.hideLoading(OAppUtil.ON_FINISH_FAILED)
                        }

                        subscriberHandler(throwable, successUnit, errorUnit)
                    })
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }

    fun getSearchPost(spec: LocationWithQuerySpec) {
        try {
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
                    .subscribe { homeResponse, throwable ->
                        val queryCompleted: ()->Unit = { viewSearchService.onQueryCompleted(homeResponse) }
                        val queryFailed: ()->Unit = { viewSearchService.onQueryFailed() }

                        subscriberHandler(throwable, queryCompleted, queryFailed)
                    })
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }

    private fun getHomeContent(locationSpec: LocationSpec, token: String) : Single<HomeResponseZip> {
        return Single.zip(
                APIRepository.create().post(locationSpec, token),
                APIRepository.create().getPeopleConnectedCount(locationSpec, token),
                APIRepository.create().getUserProfile(UserProfileSpec(OAppUserUtil.getUserId()), token),
                APIRepository.create().getPushNotificationList(getHeaderAuth()),
                Function4<HomeResponse, UserConnectedCountResponse, UserProfileResponse, PushNotificationResponse, HomeResponseZip> {
                    t1, t2, t3, t4 ->
                    createDetailModel(t1, t2, t3, t4)
                })
    }

    private fun createDetailModel(homeResponse: HomeResponse, userConnectedCountResponse: UserConnectedCountResponse, userProfileResponse: UserProfileResponse, pushNotificationResponse: PushNotificationResponse) : HomeResponseZip {
        return HomeResponseZip(homeResponse, userConnectedCountResponse, userProfileResponse, pushNotificationResponse)
    }

}