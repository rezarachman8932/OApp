package com.app.o.base.service

import com.app.o.api.comment.SubmitCommentResponse

interface OAppSubmitMessageService {
    fun onMessageBeingProcessed()
    fun onMessageNotSent()
    fun onMessageSent(submitMessageResponse: SubmitCommentResponse)
}