package com.app.o.api.comment

import com.app.o.base.data.OAppResponse

data class CommentResponse (
        override val status: Int,
        override val message: String,
        val data: List<Comment>) : OAppResponse