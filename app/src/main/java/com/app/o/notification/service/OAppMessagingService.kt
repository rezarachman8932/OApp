package com.app.o.notification.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.util.Log
import com.app.o.R
import com.app.o.notification.page.NotificationListActivity
import com.app.o.shared.util.OAppUserUtil
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.text.SimpleDateFormat
import java.util.*


class OAppMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String?) {
        super.onNewToken(token)

        Log.i("onNewToken", token)

        OAppUserUtil.setFCMToken(token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        Log.i("onMessageReceived", remoteMessage.from)

        if (remoteMessage.data.isNotEmpty()) {
            Log.i("Data", "Message Data Payload: " + remoteMessage.data)
        }

        if (remoteMessage.notification != null) {
            Log.i("Notification", "Message Notification Body: " + remoteMessage.notification?.body)
        }

        sendNotification()
    }

    private fun sendNotification() {
        val intent = Intent(this, NotificationListActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent: PendingIntent? = TaskStackBuilder.create(this).run {
            addNextIntentWithParentStack(intent)
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        val channelId = getString(R.string.text_notification_channel_id)
        val channelName = getString(R.string.text_notification_channel_name)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_logo)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(generateNotificationID(), notificationBuilder.build())
    }

    private fun generateNotificationID(): Int {
        val now = Date()
        return Integer.parseInt(SimpleDateFormat("yyMMddHHmmssZ", Locale.getDefault()).format(now))
    }

}