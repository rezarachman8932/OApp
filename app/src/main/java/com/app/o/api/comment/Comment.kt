package com.app.o.api.comment

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Comment (
        val post_id: Int,
        val user_id: Int,
        val username: String,
        val avatar: String?,
        val content: String,
        val created_at: String) : Parcelable