package com.app.o.api.detail

import com.app.o.api.media.MediaItem
import com.app.o.base.data.OAppResponse

data class DetailResponse (
        val post_id: Int,
        val title: String,
        val subtitle: String,
        val content: String,
        val type: String,
        val view_count: Int,
        val like_count: Int,
        val comment_count: Int,
        val created_at: String,
        val media: List<MediaItem>,
        override val status: Int,
        override val message: String) : OAppResponse