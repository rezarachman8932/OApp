package com.app.o.message

import com.app.o.api.APIRepository
import com.app.o.api.comment.CommentResponse
import com.app.o.api.comment.CommentSpec
import com.app.o.api.detail.DetailSpec
import com.app.o.base.presenter.OAppPresenter
import com.app.o.base.service.OAppSubmitMessageService
import com.app.o.base.service.OAppViewService
import com.app.o.shared.OAppUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

class MessagePresenter(private val view: OAppViewService<CommentResponse>,
                       private val commentView: OAppSubmitMessageService,
                       private val compositeDisposable: CompositeDisposable) : OAppPresenter() {

    fun postReplyComment(spec: CommentSpec) {
        compositeDisposable.add(APIRepository.create().submitNewComment(spec, getHeaderAuth())
                .subscribeOn(Schedulers.io())
                .compose {
                    it.observeOn(AndroidSchedulers.mainThread())
                }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    commentView.onMessageBeingProcessed()
                }
                .doOnError{
                    commentView.onMessageNotSent()
                    it.printStackTrace()
                }
                .subscribe(Consumer {
                    commentView.onMessageSent()
                }))
    }

    fun getCommentReplies(postId: String) {
        compositeDisposable.add(APIRepository.create().getDetailCommentList(DetailSpec(postId), getHeaderAuth())
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

}