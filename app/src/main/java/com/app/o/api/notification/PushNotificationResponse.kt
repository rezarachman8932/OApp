package com.app.o.api.notification

import com.app.o.base.data.OAppResponse

data class PushNotificationResponse(override val status: Int, override val message: String, val data: MutableList<PushNotificationItem>) : OAppResponse