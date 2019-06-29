package com.example.nuagemobilealarms

import android.app.Service
import android.content.Intent
import android.os.IBinder


class CloseService : Service() {

    override fun onBind(intent: Intent): IBinder? = null

    override fun onTaskRemoved(rootIntent: Intent) {
        println("onTaskRemoved called")
        super.onTaskRemoved(rootIntent)
        //do something you want before app closes.
        //stop service
        this.stopSelf()
    }
}