package com.app.o.api.home

import com.app.o.api.relation.UserConnectedCountResponse

data class HomeResponseZip (
        val homeResponse: HomeResponse,
        val userConnectedCount: UserConnectedCountResponse)