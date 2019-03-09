package com.app.o.shared.util

import android.app.ActivityManager
import android.content.Context
import android.os.Build
import java.text.SimpleDateFormat
import java.util.*
import android.app.NotificationManager
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
            val now = Date()
            return Integer.parseInt(SimpleDateFormat("yyMMddHHmmssZ", Locale.getDefault()).format(now))
        }

        fun clearNotifications(context: Context) {
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.cancelAll()
        }

        fun isAppIsInBackground(context: Context): Boolean {
            var isInBackground = true
            val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
                val runningProcesses = am.runningAppProcesses

                for (processInfo in runningProcesses) {
                    if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                        for (activeProcess in processInfo.pkgList) {
                            if (activeProcess == context.packageName) {
                                isInBackground = false
                            }
                        }
                    }
                }
            } else {
                val taskInfo = am.getRunningTasks(1)
                val componentInfo = taskInfo[0].topActivity

                if (componentInfo.packageName == context.packageName) {
                    isInBackground = false
                }
            }

            return isInBackground
        }
    }

}