package com.app.o.api.user.profile

import com.app.o.base.data.OAppResponse

data class UserProfileResponse (
        val name: String,
        val location: String?,
        val latitude: String?,
        val longitude: String?,
        val website: String?,
        val instagram: String?,
        val facebook: String?,
        val twitter: String?,
        val range_finder: Int,
        val avatar: String?,
        override val status: Int,
        override val message: String) : OAppResponse