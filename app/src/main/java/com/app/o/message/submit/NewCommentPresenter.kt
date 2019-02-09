package com.app.o.message.submit

import com.app.o.api.APIRepository
import com.app.o.api.comment.CommentSpec
import com.app.o.api.comment.SubmitCommentResponse
import com.app.o.base.presenter.OAppPresenter
import com.app.o.base.service.OAppViewService
import com.app.o.shared.util.OAppUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

class NewCommentPresenter(private val view: OAppViewService<SubmitCommentResponse>,
                          private val compositeDisposable: CompositeDisposable) : OAppPresenter() {

    fun submitNewComment(spec: CommentSpec) {
        compositeDisposable.add(APIRepository.create().submitNewComment(spec, getHeaderAuth())
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