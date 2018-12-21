package com.app.o.api.home

data class HomePostItem (
        val post_id: Int,
        val title: String,
        val subtitle: String,
        val content: String,
        val type: String,
        val view_count: Int,
        val like_count: Int,
        val comment_count: Int,
        val created_at: String,
        val media_url: String
)