package com.app.o.detail

interface DetailInteractionCallback {
    fun onIntegrationFailed()
    fun onIntegrationSucceed(resultCode: Int)
}