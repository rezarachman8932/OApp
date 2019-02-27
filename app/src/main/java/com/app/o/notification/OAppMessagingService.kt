package com.app.o.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.util.Log
import com.app.o.R
import com.app.o.home.HomeActivity
import com.app.o.shared.util.OAppUserUtil
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


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
            Log.d("RemoteMessage", "Message Data Payload: " + remoteMessage.data)
        }

        if (remoteMessage.notification != null) {
            Log.d("Notification", "Message Notification Body: " + remoteMessage.notification?.body)
        }

        sendNotification(remoteMessage)
    }

    private fun sendNotification(remoteMessage: RemoteMessage) {
        val intent = Intent(this, HomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent, PendingIntent.FLAG_ONE_SHOT)
        val channelId = "com.app.o.notification"
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_logo)
                .setContentTitle(remoteMessage.data["title"])
                .setContentText(remoteMessage.data["content"])
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "OApp Push Notification", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(1 /* ID of notification */, notificationBuilder.build())
    }

}