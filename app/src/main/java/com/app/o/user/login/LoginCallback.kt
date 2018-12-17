package com.app.o.user.login

interface LoginCallback {
    fun onEmptyInput()
    fun onUsernameNotComplete()
    fun onPasswordNotComplete()
}