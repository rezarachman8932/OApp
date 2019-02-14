package com.app.o.api.user.blocked

import com.app.o.base.data.OAppResponse

data class UserBlockingResponse(override val status: Int, override val message: String) : OAppResponse