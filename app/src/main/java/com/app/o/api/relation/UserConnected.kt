package com.app.o.api.relation

data class UserConnected (
        val user_id: Int,
        val name: String,
        val avatar: String?,
        val latitude: String?,
        val longitude: String?,
        val username: String,
        val is_starred: Boolean
)