package com.app.o.base.service

import com.app.o.api.home.HomeResponse

interface OAppSearchService {
    fun onQueryProcessed()
    fun onQueryFailed()
    fun onQueryCompleted(response: HomeResponse)
}