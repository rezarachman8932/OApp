package com.app.o.api.login

import com.app.o.base.data.OAppResponse

data class LoginResponse (
        val username: String,
        val token: String,
        val email: String,
        override val status: Int,
        override val message: String) : OAppResponse