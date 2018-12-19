package com.app.o.user.register

interface RegisterCallback {
    fun onAllInputEmpty()
    fun onNameNotComplete()
    fun onPhoneNumberNotComplete()
    fun onEmailNotValid()
    fun onUsernameNotComplete()
    fun onPasswordNotComplete()
}