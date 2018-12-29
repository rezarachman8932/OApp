package com.app.o.api.relation

import com.app.o.base.data.OAppResponse

data class UserConnectedCountResponse (val amount: Int,
                                       override val status: Int,
                                       override val message: String) : OAppResponse