package com.app.o.detail

import com.app.o.api.APIRepository
import com.app.o.api.comment.CommentResponse
import com.app.o.api.detail.DetailResponse
import com.app.o.api.detail.DetailResponseZip
import com.app.o.api.detail.DetailSpec
import com.app.o.base.service.OAppViewService
import com.app.o.shared.OAppUtil
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function3
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

class DetailPresenter(private val view: OAppViewService<DetailResponseZip>,
                      private val compositeDisposable: CompositeDisposable) {

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

    private fun getAllContent(detailSpec: DetailSpec) : Single<DetailResponseZip> {
        return Single.zip(
                APIRepository.create().getDetailContent(detailSpec, getJWTToken(OAppUtil.getUserName(), OAppUtil.getToken())),
                APIRepository.create().getDetailCommentList(detailSpec, getJWTToken(OAppUtil.getUserName(), OAppUtil.getToken())),
                APIRepository.create().getDetailCommentList(detailSpec, getJWTToken(OAppUtil.getUserName(), OAppUtil.getToken())),
                Function3<DetailResponse, CommentResponse, CommentResponse, DetailResponseZip> {
                    t1, t2, t3 ->
                    createDetailModel(t1, t2, t3)
                })
    }

    private fun createDetailModel(detailResponse: DetailResponse, commentResponse: CommentResponse, commentResponseOptional: CommentResponse) : DetailResponseZip {
        return DetailResponseZip(detailResponse, commentResponse, commentResponseOptional)
    }

    private fun getJWTToken(username: String?, token: String?): String {
        return OAppUtil.generateJWTToken(username, token)
    }

}