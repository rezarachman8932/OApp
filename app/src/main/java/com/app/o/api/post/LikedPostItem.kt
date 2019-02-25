package com.app.o.api.post

data class LikedPostItem (val post_id: Int,
                          val user_id: Int,
                          val username: String,
                          val avatar: String?,
                          val is_blocked_user: Boolean,
                          val created_at: String)