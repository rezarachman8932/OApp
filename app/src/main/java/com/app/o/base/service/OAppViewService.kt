package com.app.o.base.service

interface OAppViewService<in T> {
    fun showLoading()
    fun hideLoading()
    fun onDataResponse(data: T)
}