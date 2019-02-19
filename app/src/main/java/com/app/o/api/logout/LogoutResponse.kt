package com.app.o.api.logout

import com.app.o.base.data.OAppResponse

data class LogoutResponse (override val status: Int, override val message: String) : OAppResponse