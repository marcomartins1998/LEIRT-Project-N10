package com.example.nuagemobilealarms

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Base64
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import com.example.nuagemobilealarms.connect.VolleyHelper
import com.example.nuagemobilealarms.connect.VolleySingleton
import com.example.nuagemobilealarms.dto.Properties
import com.example.nuagemobilealarms.helper.AndroidHelper
import com.example.nuagemobilealarms.helper.FileHelper

class LoginActivity: AppCompatActivity() {
    lateinit var vs: VolleySingleton
    lateinit var vh: VolleyHelper
    lateinit var fh: FileHelper

    lateinit var companyname: EditText
    lateinit var username: EditText
    lateinit var password: EditText
    lateinit var remembermecheck: CheckBox
    lateinit var enterbutton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        vs = VolleySingleton.getInstance(this.applicationContext)
        vh = VolleyHelper(this, intent, vs)
        fh = FileHelper(this.applicationContext)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

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
                //intent.putExtra("initauth", Base64.encodeToString("${username.text.trim()}:${password.text!!.trim()}".toByteArray(), Base64.DEFAULT))
                extras = intent.extras!!

                vh.NuageAuthRequest(url){
                    val apiKey = it?.getJSONObject(0)?.getString("APIKey")
                    val apiKeyExpiry = it?.getJSONObject(0)?.getLong("APIKeyExpiry")
                    val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                    intent.putExtra("auth", "Basic "+Base64.encodeToString("${username.text.trim()}:$apiKey".toByteArray(), 0))
                    intent.putExtra("authexpiry", apiKeyExpiry)
                    intent.putExtras(extras)

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
        vs.requestQueue.cancelAll("cancelAll")
    }

}