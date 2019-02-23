package com.app.o.user.update_password

interface UpdatePasswordCallback {
    fun onErrorAllFieldsRequired()
    fun onErrorCurrentPassword()
    fun onErrorCurrentPasswordEqualsNewPassword()
    fun onErrorNotMatchedRetype()
    fun onErrorPasswordLessThanMinChars()
}