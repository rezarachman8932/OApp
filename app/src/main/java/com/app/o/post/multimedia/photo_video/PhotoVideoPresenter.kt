package com.app.o.post.multimedia.photo_video

import com.app.o.api.post.CreatedPostResponse
import com.app.o.base.service.OAppViewService
import com.app.o.post.multimedia.MultimediaPresenter
import io.reactivex.disposables.CompositeDisposable

class PhotoVideoPresenter(
        view: OAppViewService<CreatedPostResponse>,
        compositeDisposable: CompositeDisposable) : MultimediaPresenter(view, compositeDisposable)