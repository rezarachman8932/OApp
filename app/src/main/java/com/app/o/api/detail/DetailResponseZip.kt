package com.app.o.api.detail

import com.app.o.api.comment.CommentResponse

data class DetailResponseZip (
        val detailContent: DetailResponse,
        val commentListOptional: CommentResponse)