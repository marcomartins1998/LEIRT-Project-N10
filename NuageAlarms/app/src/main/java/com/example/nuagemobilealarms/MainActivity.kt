package com.example.nuagemobilealarms

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import com.android.volley.Request
import com.android.volley.toolbox.Volley
import com.example.nuagemobilealarms.connect.NukeSSLCerts
import com.example.nuagemobilealarms.connect.VolleyHelper
import com.example.nuagemobilealarms.connect.VolleySingleton
import com.example.nuagemobilealarms.helper.AndroidHelper
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity(){
    lateinit var vs: VolleySingleton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //TODO REMOVE IN FINAL PRODUCT!!!
        NukeSSLCerts.nuke()

        vs = VolleySingleton.getInstance(this.applicationContext)
        val vh = VolleyHelper(this, intent, vs)

        //TODO testar com maus inputs
        //val servername = findViewById<EditText>(R.id.serverNameIpInput).text.toString().trim().split("/")[0]
        //val ip = findViewById<EditText>(R.id.serverNameIpInput).text.toString().trim().split("/")[1]
        val servernameip = findViewById<EditText>(R.id.serverNameIpInput)
        val port = findViewById<EditText>(R.id.serverPortInput)
        val enter = findViewById<Button>(R.id.enterButton)

        enter.setOnClickListener{
            if(servernameip != null && port.text != null){
                //TODO verificar formato da string de input para evitar problemas de segurança
                val servername = servernameip.text.trim().split("/")[0]
                val ip = servernameip.text.trim().split("/")[1]
                val url = "https://$ip:${port.text}/nuage"

                vh.NuageVersionRequest(url){
                    val currver = it?.getJSONArray("versions")?.findFirst{it.opt("status")=="CURRENT"}
                    val intent = Intent(this@MainActivity, LoginActivity::class.java)
                    intent.putExtra("servername", servername)
                    intent.putExtra("url", url+"/api/${currver?.opt("version")}")
                    startActivity(intent)
                }
            }
            else {
                AndroidHelper().toastMessage(this, "Please fill all input fields.")
            }
        }
    }

    override fun onStop() {
        super.onStop()
        vs.requestQueue.cancelAll("cancelAll")
    }

    /*fun JSONArray.forEach(t: (JSONObject) -> Unit) {
        var index = 0
        while(index < this.length()){
            t(this.optJSONObject(index))
            index++
        }
    }*/

    fun JSONArray.findFirst(pred: (JSONObject) -> Boolean): JSONObject?{
        var index = 0
        while(index < this.length()){
            if(pred(this.optJSONObject(index))) return this.optJSONObject(index)
            index++
        }
        return null
    }

}
