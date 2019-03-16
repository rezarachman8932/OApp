package com.app.o.notification.page

interface NotificationListCallback {
    fun onSetAsReadProgress()
    fun onSetAsReadComplete(statusCode: Int)
}