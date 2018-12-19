package com.app.o.base.service

interface OAppViewService<in T> {
    fun showLoading()
    fun hideLoading(statusCode: Int)
    fun onDataResponse(data: T)
}