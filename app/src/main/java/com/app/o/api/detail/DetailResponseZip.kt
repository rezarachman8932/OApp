package com.app.o.api.detail

import com.app.o.api.comment.CommentResponse
import com.app.o.api.post.LikedPostListResponse

data class DetailResponseZip (
        val detailContent: DetailResponse,
        val likedPostListResponse: LikedPostListResponse,
        val commentListOptional: CommentResponse)