package com.app.o.api.relation

import com.app.o.base.data.OAppResponse

data class UserConnectedResponse (
        val data: List<UserConnected>,
        override val status: Int,
        override val message: String) : OAppResponse