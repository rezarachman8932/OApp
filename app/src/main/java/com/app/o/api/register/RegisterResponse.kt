package com.app.o.api.register

import com.app.o.base.data.OAppResponse

data class RegisterResponse (override val status: Int, override val message: String) : OAppResponse