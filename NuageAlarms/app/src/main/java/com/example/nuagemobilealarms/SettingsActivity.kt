package com.example.nuagemobilealarms

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Base64
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.example.nuagemobilealarms.connect.VolleySingleton
import com.example.nuagemobilealarms.dto.Properties
import com.example.nuagemobilealarms.helper.AndroidHelper
import com.example.nuagemobilealarms.helper.FileHelper
import com.example.nuagemobilealarms.helper.VolleyHelper
import org.json.JSONArray
import org.json.JSONObject

class SettingsActivity : AppCompatActivity() {
    val TAG = "SettingsActivity"

    lateinit var vs: VolleySingleton
    lateinit var vh: VolleyHelper
    lateinit var fh: FileHelper

    //Server
    lateinit var servernameip: EditText
    lateinit var port: EditText
    //Account
    lateinit var companyname: EditText
    lateinit var username: EditText
    lateinit var password: EditText
    //Save
    lateinit var savebutton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        Log.d(TAG, "onCreate: started")

        vs = VolleySingleton.getInstance(this.applicationContext)
        vh = VolleyHelper(this, intent, vs)
        fh = FileHelper(this.applicationContext)

        servernameip = findViewById(R.id.serverNameIpInput)
        port = findViewById(R.id.serverPortInput)
        companyname = findViewById(R.id.companyNameInput)
        username = findViewById(R.id.userNameInput)
        password = findViewById(R.id.passWordInput)
        savebutton = findViewById(R.id.saveButton)

        fh.getProperties().assignToMain(servernameip, port)
        fh.getProperties().assignToLogin(companyname, username)

        val servernameipregex = """.[^/]*/\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3}""".toRegex()
        val portregex = """\d{1,5}""".toRegex()

        savebutton.setOnClickListener {
            if (servernameipregex.matches(servernameip.text) && portregex.matches(port.text) && username.text != null && password.text != null && companyname.text != null) {
                val servername = servernameip.text.trim().split("/")[0]
                val ip = servernameip.text.trim().split("/")[1]
                var url = "https://$ip:${port.text}/nuage"

                vh.NuageVersionRequest(url, TAG).thenApply {
                    val currver = it?.getJSONArray("versions")?.findFirst { it.opt("status") == "CURRENT" }
                    url += "/api/${currver?.opt("version")}"
                    intent.putExtra("servername", servername)
                    intent.putExtra("url", url)
                }.thenCompose { vh.NuageAuthRequest(url, TAG) }.thenAccept {
                    intent.putExtra("companyname", companyname.text.trim().toString())
                    intent.putExtra("username", username.text.trim().toString())
                    intent.putExtra("password", password.text.toString())

                    val apiKey = it?.getJSONObject(0)?.getString("APIKey")
                    val apiKeyExpiry = it?.getJSONObject(0)?.getLong("APIKeyExpiry")
                    intent.putExtra(
                        "auth",
                        "Basic " + Base64.encodeToString("${username.text.trim()}:$apiKey".toByteArray(), 0)
                    )
                    intent.putExtra("authexpiry", apiKeyExpiry)

                    fh.putProperties(
                        Properties(
                            servernameip = servernameip.text.toString(),
                            port = port.text.toString(),
                            companyname = companyname.text.trim().toString(),
                            username = username.text.trim().toString()
                        )
                    )
                    when (intent.extras!!.getString("parentActivity")) {
                        "AlarmFiltersActivity" -> startActivity(
                            Intent(
                                this@SettingsActivity,
                                AlarmFiltersActivity::class.java
                            ).putExtras(intent.extras!!)
                        )
                        "NotificationFiltersActivity" -> startActivity(
                            Intent(
                                this@SettingsActivity,
                                NotificationFiltersActivity::class.java
                            ).putExtras(intent.extras!!)
                        )
                    }

                }
            } else {
                AndroidHelper.toastMessage(this, "Please fill all input fields with valid information.")
            }
        }
    }

    fun JSONArray.findFirst(pred: (JSONObject) -> Boolean): JSONObject? {
        var index = 0
        while (index < this.length()) {
            if (pred(this.optJSONObject(index))) return this.optJSONObject(index)
            index++
        }
        return null
    }
}