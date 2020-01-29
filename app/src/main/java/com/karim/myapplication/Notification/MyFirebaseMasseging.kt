package com.karim.myapplication.Notification

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.karim.myapplication.Activites.MainActivity
import com.karim.myapplication.R


class MyFirebaseMasseging :FirebaseMessagingService() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onMessageReceived(p0: RemoteMessage?) {
        super.onMessageReceived(p0)
            sendNotification(p0!!)

    }

    @SuppressLint("WrongConstant")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun sendNotification(p0: RemoteMessage) {
        val name=p0.data["name"]
        val date=p0.data["orderDate"]
        val user: String = p0.data["user"]!!
        val j = user.replace("[\\D]".toRegex(), "").toInt()
        var notificaiton=p0.notification
        var intent=Intent(this,MainActivity::class.java)
        var bundle=Bundle()
        bundle.putString("name",name)
        bundle.putString("date",date)
        var str=" طلب للعميل "
        str+=name
        str+=" بتاريخ "
        str+=date
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this,j, intent, PendingIntent.FLAG_ONE_SHOT)
        var builder=NotificationCompat.Builder(baseContext)
            .setSmallIcon(R.drawable.nlogo)
            .setContentTitle(getString(R.string.new_order))
            .setContentText(str)
            .setPriority(Notification.PRIORITY_MAX)
            .setContentIntent(pendingIntent)
        val manager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val mp: MediaPlayer = MediaPlayer.create(this, R.raw.eventually)
        mp.start()
        manager.notify(73195, builder.build())
        var noti=getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        var cahnnel_id="channel_id"
        var notificationChannel=NotificationChannel(cahnnel_id,"channel human readable",1)
        noti.createNotificationChannel(notificationChannel)
        builder.setChannelId(cahnnel_id)
        var i = 0
        if (j > 0) i = j
        noti.notify(i, builder.build())

    }
}