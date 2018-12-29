package com.app.o.api.home

import com.app.o.base.data.OAppResponse

data class HomeResponse (override val status: Int,
                         override val message: String,
                         val data: List<HomePostItem>) : OAppResponse