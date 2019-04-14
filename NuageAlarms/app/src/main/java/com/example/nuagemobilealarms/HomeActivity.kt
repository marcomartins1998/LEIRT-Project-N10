package com.example.nuagemobilealarms

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.example.nuagemobilealarms.connect.VolleyHelper
import com.example.nuagemobilealarms.connect.VolleySingleton

class HomeActivity : AppCompatActivity() {
    val TAG = "HomeActivity"
    lateinit var vs: VolleySingleton
    lateinit var vh: VolleyHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        Log.d(TAG, "onCreate: started")

        vs = VolleySingleton.getInstance(this.applicationContext)
        vh = VolleyHelper(this, intent, vs)

        val subtitle = findViewById<TextView>(R.id.subTitle)
        val alarmlistbutton = findViewById<Button>(R.id.button_alarmlist)
        val alarmnotificationsbutton = findViewById<Button>(R.id.button_alarmnotifications)
        val nuagedescbutton = findViewById<Button>(R.id.button_nuagedescription)

        val extras: Bundle = intent.extras!!
        val servername = extras.getString("servername")
        val url = extras.getString("url")!!
        //val ip = extras.getString("ip")
        //val port = extras.getString("port")
        subtitle.text = "Connected to: $servername"
        //val url = "https://$ip:$port/"

        alarmlistbutton.setOnClickListener {
            val intent = Intent(this@HomeActivity, AlarmFiltersActivity::class.java)
            intent.putExtras(extras)
            startActivity(intent)
        }



    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop: started")
        vs.requestQueue.cancelAll(TAG)
    }

}