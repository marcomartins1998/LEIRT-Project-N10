package com.example.nuagemobilealarms

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log

class DescriptionActivity : AppCompatActivity() {
    val TAG = "DescriptionActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_description)
        Log.d(TAG, "onCreate: started")
    }
}