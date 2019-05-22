package com.app.o.post.multimedia

import com.app.o.api.APIRepository
import com.app.o.api.post.CreatedPostResponse
import com.app.o.base.presenter.OAppPresenter
import com.app.o.base.service.OAppViewService
import com.app.o.shared.util.OAppUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody
import okhttp3.RequestBody

abstract class MultimediaPresenter(
        private val view: OAppViewService<CreatedPostResponse>,
        private val compositeDisposable: CompositeDisposable) : OAppPresenter() {

    open val files = arrayListOf<MultipartBody.Part>()

    open fun createPost(
            title: RequestBody,
            subtitle: RequestBody,
            type: RequestBody,
            latitude: RequestBody,
            longitude: RequestBody,
            content: RequestBody) {
        try {
            compositeDisposable.add(APIRepository.create().createPost(files, title, subtitle, type, latitude, longitude, content, getHeaderAuth())
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
                    })
            )
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }

}