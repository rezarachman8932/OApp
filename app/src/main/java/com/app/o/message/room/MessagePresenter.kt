package com.app.o.message.room

import com.app.o.api.APIRepository
import com.app.o.api.comment.CommentSpec
import com.app.o.base.presenter.OAppPresenter
import com.app.o.base.service.OAppSubmitMessageService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MessagePresenter(private val commentView: OAppSubmitMessageService,
                       private val compositeDisposable: CompositeDisposable) : OAppPresenter() {

    fun postReplyComment(spec: CommentSpec) {
        try {
            compositeDisposable.add(APIRepository.create().submitReplyComment(spec, getHeaderAuth())
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
                    .subscribe { submitCommentResponse, throwable ->
                        val succeed = { commentView.onMessageSent(submitCommentResponse) }
                        val failed = { commentView.onMessageNotSent() }

                        subscriberHandler(throwable, succeed, failed)
                    }
            )
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }

}