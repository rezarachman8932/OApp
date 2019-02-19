package com.app.o.user.logout

interface LogoutCallback {
    fun onLogoutSucceed()
    fun onLogoutFailed()
    fun onLogoutProceed()
}