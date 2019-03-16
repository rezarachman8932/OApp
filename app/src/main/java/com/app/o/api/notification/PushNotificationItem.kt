package com.app.o.api.notification

data class PushNotificationItem(
        val notification_id: Int,
        val user_id: Int,
        val ref_id: Int,
        val action_type: String,
        val is_read: Boolean,
        val created_at: String?,
        val username: String,
        val message: String)