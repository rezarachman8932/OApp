package com.app.o.detail

import com.app.o.api.APIRepository
import com.app.o.api.comment.CommentResponse
import com.app.o.api.detail.DetailResponse
import com.app.o.api.detail.DetailResponseZip
import com.app.o.api.detail.DetailSpec
import com.app.o.api.user.profile.UserProfileResponse
import com.app.o.base.presenter.OAppPresenter
import com.app.o.base.service.OAppViewService
import com.app.o.shared.util.OAppUtil
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function3
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

class DetailPresenter(private val view: OAppViewService<DetailResponseZip>,
                      private val compositeDisposable: CompositeDisposable) : OAppPresenter() {

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
                APIRepository.create().getDetailContent(detailSpec, getHeaderAuth()),
                APIRepository.create().getOwnProfile(getHeaderAuth()),
                APIRepository.create().getDetailCommentList(detailSpec, getHeaderAuth()),
                Function3<DetailResponse, UserProfileResponse, CommentResponse, DetailResponseZip> {
                    t1, t2, t3 ->
                    createDetailModel(t1, t2, t3)
                })
    }

    private fun createDetailModel(detailResponse: DetailResponse, userProfileResponse: UserProfileResponse, commentResponseOptional: CommentResponse) : DetailResponseZip {
        return DetailResponseZip(detailResponse, userProfileResponse, commentResponseOptional)
    }

}