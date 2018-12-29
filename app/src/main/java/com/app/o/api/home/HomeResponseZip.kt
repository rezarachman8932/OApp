package com.app.o.api.home

import com.app.o.api.relation.UserConnectedCountResponse
import com.app.o.api.relation.UserConnectedResponse

data class HomeResponseZip (
        val homeResponse: HomeResponse,
        val userConnectedResponse: UserConnectedResponse,
        val userConnectedCount: UserConnectedCountResponse)