package com.app.o.api.user.blocked

import com.app.o.api.relation.UserConnected
import com.app.o.base.data.OAppResponse

data class BlockedUserResponse (
        val data: MutableList<UserConnected>,
        override val status: Int,
        override val message: String) : OAppResponse