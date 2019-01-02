package com.app.o.api.post

import com.app.o.base.data.OAppResponse

data class CreatedPostResponse (
        val post_id: String,
        val type: String,
        override val status: Int,
        override val message: String) : OAppResponse