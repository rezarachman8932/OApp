package com.app.o.api.comment

import com.app.o.base.data.OAppResponse

data class SubmitCommentResponse(
        val comment_id: Int,
        val post_id: Int,
        val user_id: Int,
        val username: String,
        val avatar: String?,
        val content: String,
        val created_at: String,
        override val status: Int,
        override val message: String) : OAppResponse