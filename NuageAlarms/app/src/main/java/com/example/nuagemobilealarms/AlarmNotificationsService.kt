package com.example.nuagemobilealarms

import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Intent
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import com.example.nuagemobilealarms.helper.FileHelper
import com.example.nuagemobilealarms.model.Alarm
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class AlarmNotificationsService : FirebaseMessagingService() {
    val fh = FileHelper(this)

    override fun onMessageReceived(rmsg: RemoteMessage?) {
        //TODO changed por testing purposes
        val props = fh.getProperties()
        val ip = if (props.noneNullOrEmpty()) props.servernameip!!.trim().split("/")[1] else "NoIP"
        val username = if (props.noneNullOrEmpty()) props.username!! else "NoUsername"
        val filters = fh.getFilters(ip, username)
        //var entitylist: List<EntityDto>
        //if (props.noneNullOrEmpty()) entitylist = fh.getEntityList(props.servernameip!!.trim().split("/")[1], props.username!!)
        //else entitylist = fh.getEntityList("NoIP", "NoUsername")
        //val entity = entitylist.filter {it.id == rmsg?.data?.get("parentID")}
        //val entity = arrayListOf(EntityDto("jkfbhjvgfhjf", null, "TestEnterprise", Type.ENTERPRISE, true))
        val entity = filters?.entityList?.filter { it.id == rmsg?.data?.get("parentID") } ?: arrayListOf()
        if (filters?.severity == "ALL" || (filters?.severity == rmsg?.data?.get("severity") && entity.isNotEmpty())) {
            val resultIntent = Intent(this, MainActivity::class.java)
            val resultPendingIntent: PendingIntent? = TaskStackBuilder.create(this).run {
                addNextIntentWithParentStack(resultIntent)
                getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
            }
            val notificationManager = NotificationManagerCompat.from(this)
            val notification = NotificationCompat.Builder(this, NuageAlarmsApp.CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_alarm)
                .setContentTitle(
                    "${rmsg?.data?.get("severity")} Alarm in ${if (entity.isNotEmpty()) entity[0].name else rmsg?.data?.get(
                        "parentID"
                    )}"
                )
                .setContentText(rmsg?.data?.get("description"))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setContentIntent(resultPendingIntent)
                .build()
            if (props.noneNullOrEmpty()) fh.addToAlarmList(
                Alarm.mapToAlarm(rmsg!!.data),
                props.servernameip!!.trim().split("/")[1],
                props.username!!
            )
            notificationManager.notify(1, notification)
        }
    }

}