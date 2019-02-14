package com.app.o.user.blocked

import com.app.o.api.user.blocked.UserBlockingResponse

interface UnblockedAccountCallback {
    fun onProgress()
    fun onSucceed(response: UserBlockingResponse)
    fun onFailed()
}