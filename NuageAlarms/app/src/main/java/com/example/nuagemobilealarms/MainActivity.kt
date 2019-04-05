package com.example.nuagemobilealarms

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import com.example.nuagemobilealarms.connect.NukeSSLCerts
import com.example.nuagemobilealarms.connect.VolleyHelper
import com.example.nuagemobilealarms.connect.VolleySingleton
import com.example.nuagemobilealarms.helper.AndroidHelper
import com.example.nuagemobilealarms.helper.FileHelper
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity(){
    val vs = VolleySingleton(this.applicationContext)
    val vh = VolleyHelper(this, intent, vs)
    val fh = FileHelper(this)

    lateinit var servernameip: EditText
    lateinit var port: EditText
    lateinit var remenbercheck: CheckBox
    lateinit var enter: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //TODO REMOVE IN FINAL PRODUCT!!!
        NukeSSLCerts.nuke()

        //vs = VolleySingleton.getInstance(this.applicationContext)
        //vh = VolleyHelper(this, intent, vs)

        //TODO testar com maus inputs
        servernameip = findViewById(R.id.serverNameIpInput)
        port = findViewById(R.id.serverPortInput)
        remenbercheck = findViewById(R.id.rememberMeCheck)
        enter = findViewById(R.id.enterButton)

        val properties = fh.getProperties()

        enter.setOnClickListener{
            if (servernameip.text != null && port.text != null) {
                //TODO verificar formato da string de input para evitar problemas de segurança
                val servername = servernameip.text.trim().split("/")[0]
                val ip = servernameip.text.trim().split("/")[1]
                val url = "https://$ip:${port.text}/nuage"

                vh.NuageVersionRequest(url){
                    val currver = it?.getJSONArray("versions")?.findFirst{it.opt("status")=="CURRENT"}
                    val intent = Intent(this@MainActivity, LoginActivity::class.java)
                    intent.putExtra("servername", servername)
                    intent.putExtra("url", url+"/api/${currver?.opt("version")}")

                    //Store properties in internal file system
                    val properties = FileHelper(this).getProperties()
                    properties.servername = servername
                    properties.url = url+"/api/${currver?.opt("version")}"
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

    fun JSONArray.findFirst(pred: (JSONObject) -> Boolean): JSONObject?{
        var index = 0
        while(index < this.length()){
            if(pred(this.optJSONObject(index))) return this.optJSONObject(index)
            index++
        }
        return null
    }

}
