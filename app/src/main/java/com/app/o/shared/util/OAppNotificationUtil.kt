package com.app.o.shared.util

import android.app.NotificationManager
import android.content.Context
import com.app.o.shared.helper.OAppPreferencesHelper

class OAppNotificationUtil {

    companion object {
        const val PUSH_NOTIFICATION = "pushNotification"

        fun isPushNotificationExist(): Boolean {
            return OAppPreferencesHelper.isNotificationExist
        }

        fun setPushNotificationExist(exist: Boolean) {
            OAppPreferencesHelper.isNotificationExist = exist
        }

        fun generateNotificationTimeID(): Int {
            val now = System.currentTimeMillis()
            return now.toInt()
        }

        fun clearNotifications(context: Context) {
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.cancelAll()
        }
    }

}