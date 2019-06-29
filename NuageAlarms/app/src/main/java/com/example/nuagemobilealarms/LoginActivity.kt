package com.example.nuagemobilealarms

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Base64
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import com.example.nuagemobilealarms.connect.VolleySingleton
import com.example.nuagemobilealarms.dto.Properties
import com.example.nuagemobilealarms.helper.AndroidHelper
import com.example.nuagemobilealarms.helper.FileHelper
import com.example.nuagemobilealarms.helper.VolleyHelper
import com.google.firebase.messaging.FirebaseMessaging

class LoginActivity: AppCompatActivity() {
    val TAG = "LoginActivity"
    lateinit var vs: VolleySingleton
    lateinit var vh: VolleyHelper
    lateinit var fh: FileHelper

    lateinit var companyname: EditText
    lateinit var username: EditText
    lateinit var password: EditText
    lateinit var remembermecheck: CheckBox
    lateinit var enterbutton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        Log.d(TAG, "onCreate: started")

        vs = VolleySingleton.getInstance(this.applicationContext)
        vh = VolleyHelper(this, intent, vs)
        fh = FileHelper(this.applicationContext)

        companyname = findViewById(R.id.companyNameInput)
        username = findViewById(R.id.userNameInput)
        password = findViewById(R.id.passWordInput)
        remembermecheck = findViewById(R.id.rememberMeCheck)
        enterbutton = findViewById(R.id.enterButton)

        fh.getProperties().assignToLogin(companyname, username)

        var extras: Bundle = intent.extras!!
        val url = extras.getString("url")!!

        enterbutton.setOnClickListener {
            if(username.text != null && password.text != null && companyname.text != null) {
                intent.putExtra("companyname", companyname.text.trim().toString())
                intent.putExtra("username", username.text.trim().toString())
                intent.putExtra("password", password.text.toString())
                extras = intent.extras!!

                vh.NuageAuthRequest(url, TAG).thenAccept {
                    val apiKey = it?.getJSONObject(0)?.getString("APIKey")
                    val apiKeyExpiry = it?.getJSONObject(0)?.getLong("APIKeyExpiry")
                    val intent = Intent(this@LoginActivity, AlarmFiltersActivity::class.java)
                    intent.putExtra("auth", "Basic "+Base64.encodeToString("${username.text.trim()}:$apiKey".toByteArray(), 0))
                    intent.putExtra("authexpiry", apiKeyExpiry)
                    intent.putExtra("parentActivity", this.javaClass.simpleName)
                    intent.putExtras(extras)

                    //Check if a different user has logged in other than the previous one(assuming there is one) and unsubscribe from the notification topic
                    if (fh.getProperties().noneNullOrEmpty() && fh.getCurrentSubscription().isNotEmpty()) {
                        val props = fh.getProperties()
                        if (props.username != username.text.toString() || props.companyname != companyname.text.toString())
                            FirebaseMessaging.getInstance().unsubscribeFromTopic(fh.getCurrentSubscription())
                    }

                    val properties: Properties = when (remembermecheck.isChecked) {
                        true -> fh.getProperties().replace(
                            companyname = companyname.text.trim().toString(),
                            username = username.text.trim().toString()
                        )
                        false -> fh.getProperties().replace(companyname = null, username = null)
                    }

                    fh.putProperties(properties)
                    startActivity(intent)
                }
            }
            else {
                AndroidHelper.toastMessage(this, "Please fill all input fields.")
            }
        }
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop: started")
        vs.requestQueue.cancelAll(TAG)
    }

}