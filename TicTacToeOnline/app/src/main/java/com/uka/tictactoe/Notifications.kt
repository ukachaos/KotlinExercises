package com.uka.tictactoe

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v4.app.NotificationCompat

class Notifications() {

    val NOTIFY_TAG = "new request"

    fun Notify(context: Context, message: String, number: Int) {

        val intent = Intent(context, LoginActivity::class.java)

        val builder = NotificationCompat.Builder(context)
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentTitle("New Request")
                .setContentText(message)
                .setNumber(number)
                .setSmallIcon(R.drawable.tictac)
                .setContentIntent(PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT))
                .setAutoCancel(true)

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR)
            notificationManager.notify(NOTIFY_TAG, 0, builder.build())
        else
            notificationManager.notify(NOTIFY_TAG.hashCode(), builder.build())
    }
}