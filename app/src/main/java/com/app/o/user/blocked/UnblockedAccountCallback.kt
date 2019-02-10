package com.app.o.user.blocked

import com.app.o.api.user.blocked.UnblockedUserResponse

interface UnblockedAccountCallback {
    fun onUnblockingAccount()
    fun onUnblockedAccountSuccceed(response: UnblockedUserResponse)
    fun onUnblockedAccountFailed()
}