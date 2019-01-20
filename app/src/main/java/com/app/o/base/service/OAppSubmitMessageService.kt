package com.app.o.base.service

interface OAppSubmitMessageService {
    fun onMessageBeingProcessed()
    fun onMessageNotSent()
    fun onMessageSent()
}