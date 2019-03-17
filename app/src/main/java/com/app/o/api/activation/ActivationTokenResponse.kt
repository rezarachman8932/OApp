package com.app.o.api.activation

import com.app.o.base.data.OAppResponse

data class ActivationTokenResponse(override val status: Int, override val message: String) : OAppResponse