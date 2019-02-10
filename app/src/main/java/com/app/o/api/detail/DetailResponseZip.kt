package com.app.o.api.detail

import com.app.o.api.comment.CommentResponse
import com.app.o.api.user.profile.UserProfileResponse

data class DetailResponseZip (
        val detailContent: DetailResponse,
        val userProfile: UserProfileResponse,
        val commentListOptional: CommentResponse)