package com.app.o.user.update_password

interface UpdatePasswordCallback {
    fun onErrorAllFieldsRequired()
    fun onErrorCurrentPasswordEqualsNewPassword()
    fun onErrorNotMatchedRetype()
    fun onErrorPasswordLessThanMinChars()
}