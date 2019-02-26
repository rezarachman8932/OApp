package com.app.o.detail

import com.app.o.api.APIRepository
import com.app.o.api.comment.CommentResponse
import com.app.o.api.detail.DetailResponse
import com.app.o.api.detail.DetailResponseZip
import com.app.o.api.detail.DetailSpec
import com.app.o.api.user.blocked.UserBlockingSpec
import com.app.o.base.presenter.OAppPresenter
import com.app.o.base.service.OAppViewService
import com.app.o.shared.util.OAppUtil
import com.app.o.user.blocked.UnblockedAccountCallback
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

class DetailPresenter(private val view: OAppViewService<DetailResponseZip>,
                      private val userBlockingCallback: UnblockedAccountCallback,
                      private val interactionCallback: DetailInteractionCallback,
                      private val compositeDisposable: CompositeDisposable) : OAppPresenter() {

    fun doLikeUserPost(postId: String, resultCode: Int) {
        compositeDisposable.add(APIRepository.create().likeUserPost(DetailSpec(postId), getHeaderAuth())
                .subscribeOn(Schedulers.io())
                .compose {
                    it.observeOn(AndroidSchedulers.mainThread())
                }
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext {
                    interactionCallback.onIntegrationFailed()
                    Single.error(it)
                }
                .doOnError{
                    interactionCallback.onIntegrationFailed()
                    it.printStackTrace()
                }
                .subscribe(Consumer {
                    interactionCallback.onIntegrationSucceed(resultCode)
                }))
    }

    fun doDislikeUserPost(postId: String, resultCode: Int) {
        compositeDisposable.add(APIRepository.create().unLikeUserPost(DetailSpec(postId), getHeaderAuth())
                .subscribeOn(Schedulers.io())
                .compose {
                    it.observeOn(AndroidSchedulers.mainThread())
                }
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext {
                    interactionCallback.onIntegrationFailed()
                    Single.error(it)
                }
                .doOnError{
                    interactionCallback.onIntegrationFailed()
                    it.printStackTrace()
                }
                .subscribe(Consumer {
                    interactionCallback.onIntegrationSucceed(resultCode)
                }))
    }

    fun geDetailPageContent(spec: DetailSpec) {
        compositeDisposable.add(getAllContent(spec)
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

    fun blockUser(spec: UserBlockingSpec) {
        compositeDisposable.add(APIRepository.create().blockedUser(spec, getHeaderAuth())
                .subscribeOn(Schedulers.io())
                .compose {
                    it.observeOn(AndroidSchedulers.mainThread())
                }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    userBlockingCallback.onProgress()
                }
                .onErrorResumeNext {
                    userBlockingCallback.onFailed()
                    Single.error(it)
                }
                .doOnError{
                    userBlockingCallback.onFailed()
                    it.printStackTrace()
                }
                .subscribe(Consumer {
                    userBlockingCallback.onSucceed(it)
                }))
    }

    private fun getAllContent(detailSpec: DetailSpec) : Single<DetailResponseZip> {
        return Single.zip(
                APIRepository.create().getDetailContent(detailSpec, getHeaderAuth()),
                APIRepository.create().getDetailCommentList(detailSpec, getHeaderAuth()),
                BiFunction<DetailResponse, CommentResponse, DetailResponseZip> {
                    t1, t2 ->
                    createDetailModel(t1, t2)
                })
    }

    private fun createDetailModel(detailResponse: DetailResponse, commentResponseOptional: CommentResponse) : DetailResponseZip {
        return DetailResponseZip(detailResponse, commentResponseOptional)
    }

}