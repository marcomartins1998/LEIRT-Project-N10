package com.example.nuagemobilealarms

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Base64
import android.widget.Button
import android.widget.EditText
import com.example.nuagemobilealarms.connect.VolleyHelper
import com.example.nuagemobilealarms.connect.VolleySingleton
import com.example.nuagemobilealarms.helper.AndroidHelper
import com.example.nuagemobilealarms.helper.FileHelper

class LoginActivity: AppCompatActivity() {
    lateinit var vs: VolleySingleton
    lateinit var vh: VolleyHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        vs = VolleySingleton.getInstance(this.applicationContext)
        vh = VolleyHelper(this, intent, vs)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val companyname = findViewById<EditText>(R.id.companyNameInput)
        val username = findViewById<EditText>(R.id.userNameInput)
        val password = findViewById<EditText>(R.id.passWordInput)
        val enterbutton = findViewById<Button>(R.id.enterButton)

        var extras: Bundle = intent.extras!!
        val url = extras.getString("url")!!

        enterbutton.setOnClickListener {
            if(username.text != null && password.text != null && companyname.text != null) {
                intent.putExtra("companyname",companyname.text.toString())
                intent.putExtra("username", username.text.trim())
                intent.putExtra("password", password.text.toString())
                //intent.putExtra("initauth", Base64.encodeToString("${username.text.trim()}:${password.text!!.trim()}".toByteArray(), Base64.DEFAULT))
                extras = intent.extras!!

                vh.NuageAuthRequest(url){
                    val apiKey = it?.getJSONObject(0)?.getString("APIKey")
                    val apiKeyExpiry = it?.getJSONObject(0)?.getLong("APIKeyExpiry")
                    val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                    intent.putExtra("auth", "Basic "+Base64.encodeToString("${username.text.trim()}:$apiKey".toByteArray(), 0))
                    intent.putExtra("authexpiry", apiKeyExpiry)
                    intent.putExtras(extras)

                    //Store properties in internal file system
                    val properties = FileHelper(this).getProperties()
                    properties.username = extras.getString("username")
                    properties.companyname = extras.getString("companyname")
                    properties.initauth = extras.getString("initauth")
                    properties.auth = intent.extras!!.getString("auth")
                    properties.authexpiry = intent.extras!!.getString("authexpiry")
                    FileHelper(this).putProperties(properties)

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
        vs.requestQueue.cancelAll("cancelAll")
    }

}