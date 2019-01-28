package com.app.o.api.comment

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Comment (
        val comment_id: Int,
        val post_id: Int,
        val user_id: Int,
        val username: String,
        val avatar: String?,
        val content: String,
        val created_at: String,
        val time_string: String?,
        val reply_count: Int?,
        val reply: MutableList<Comment>?) : Parcelable