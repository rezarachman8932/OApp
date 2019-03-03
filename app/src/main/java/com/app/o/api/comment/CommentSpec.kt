package com.app.o.api.comment

data class CommentSpec(val post_id: String,
                       val content: String,
                       val device_token: String,
                       val comment_id: String?)