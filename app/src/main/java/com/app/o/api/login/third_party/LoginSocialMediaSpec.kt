package com.app.o.api.login.third_party

data class LoginSocialMediaSpec(
        val email: String?,
        val name: String?,
        val password: String?,
        val login_type: String,
        val device_token: String?)