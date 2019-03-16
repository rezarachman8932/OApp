package com.app.o.api.notification

import com.app.o.base.data.OAppResponse

data class PushNotificationReadResponse (override val status: Int, override val message: String) : OAppResponse