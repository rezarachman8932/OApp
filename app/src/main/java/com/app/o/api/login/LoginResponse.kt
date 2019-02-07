package com.app.o.api.login

import com.app.o.base.data.OAppResponse

data class LoginResponse (
        val username: String,
        val user_id: Int,
        val token: String,
        val email: String,
        val phonenumber: String,
        override val status: Int,
        override val message: String) : OAppResponse