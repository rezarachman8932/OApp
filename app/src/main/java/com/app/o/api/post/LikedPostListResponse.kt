package com.app.o.api.post

import com.app.o.base.data.OAppResponse

data class LikedPostListResponse (
        val data: MutableList<LikedPostItem>,
        override val status: Int,
        override val message: String) : OAppResponse