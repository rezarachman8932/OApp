package com.app.o.detail

import com.app.o.api.detail.DetailResponse
import com.app.o.base.service.OAppViewService
import io.reactivex.disposables.CompositeDisposable

class DetailPresenter(private val view: OAppViewService<DetailResponse>,
                      private val compositeDisposable: CompositeDisposable) {

}