package com.example.nuagemobilealarms.connect

import android.content.Context
import android.content.Intent
import android.support.v4.content.ContextCompat.startActivity
import android.util.Base64
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.example.nuagemobilealarms.HomeActivity
import com.example.nuagemobilealarms.LoginActivity
import com.example.nuagemobilealarms.MainActivity
import com.example.nuagemobilealarms.helper.AndroidHelper
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import kotlin.text.Charsets.UTF_8

//TODO try to clean repeated code
class VolleyHelper(val context: Context, val intent: Intent, val vs: VolleySingleton){
    val TAG = "cancelAll"

    fun JSONArrayRequest(url: String, method: Int, headers: HashMap<String,String>, requestBody: JSONObject?, errorMsg: String,
                         rspAction: (JSONArray?) -> Unit): JsonArrayRequest{
        return object: JsonArrayRequest(
            method,
            url,
            null,
            Response.Listener(rspAction),
            Response.ErrorListener {
                AndroidHelper().toastMessage(context, errorMsg)
            }
        ){
            override fun getHeaders(): MutableMap<String, String> = headers
            override fun getBody(): ByteArray? = requestBody?.toString()?.toByteArray(UTF_8)
        }

    }

    fun JSONObjectRequest(url: String, method: Int, headers: HashMap<String,String>, requestBody: JSONObject?, errorMsg: String,
                          rspAction: (JSONObject?) -> Unit): JsonObjectRequest{
        return object: JsonObjectRequest(
            method,
            url,
            null,
            Response.Listener(rspAction),
            Response.ErrorListener {
                println(it)
                AndroidHelper().toastMessage(context, errorMsg)
            }
        ){
            override fun getHeaders(): MutableMap<String, String> = headers
            override fun getBody(): ByteArray? = requestBody?.toString()?.toByteArray(UTF_8)
        }
    }

    fun NuageVersionRequest(url: String, rspAction: (JSONObject?) -> Unit){
        val headers = HashMap<String, String>()
        headers["Content-type"] = "application/json"
        val jsonArr = JSONObjectRequest(url, Request.Method.GET, headers, null, "Invalid parameters/Try again later.", rspAction)
        jsonArr.tag = TAG
        vs.addToRequestQueue(jsonArr)
    }

    fun NuageAuthRequest(url: String, rspAction: (JSONArray?) -> Unit){
        val extras = intent.extras!!
        val headers = HashMap<String, String>()
        headers["X-Nuage-Organization"] = extras.getString("companyname")!!.trim()
        headers["Content-type"] = "application/json"
        headers["Authorization"] = "Basic "+ Base64.encodeToString("${extras.getString("username")!!.trim()}:${extras.getString("password")!!.trim()}".toByteArray(), Base64.DEFAULT)
        intent.putExtras(extras)

        val jsonArr = JSONArrayRequest("$url/me", Request.Method.GET, headers, null, "Login fail, please try again.", rspAction)
        jsonArr.tag = TAG
        vs.addToRequestQueue(jsonArr)
    }

    //TODO testar este método
    fun NuageAuthIfExpired(url: String, action: () -> Unit){
        val extras = intent.extras!!
        if(Calendar.getInstance().time.time > extras.getLong("authexpiry")){
            NuageAuthRequest(url){
                val apiKey = it?.getJSONObject(0)?.getString("APIKey")
                val apiKeyExpiry = it?.getJSONObject(0)?.getInt("APIKeyExpiry")
                intent.putExtra("auth", "Basic "+ Base64.encodeToString("${extras.getString("username")!!.trim()}:$apiKey".toByteArray(), 0))
                intent.putExtra("authexpiry", apiKeyExpiry)
                intent.putExtras(extras)
                action()
            }
        } else action(); intent.putExtras(extras)
    }

    fun NuageGetDomains(url: String, rspAction: (JSONArray?) -> Unit){
        NuageAuthIfExpired(url){
            val extras = intent.extras!!
            val headers = HashMap<String, String>()
            headers["X-Nuage-Organization"] = extras.getString("companyname")!!.trim()
            headers["Content-type"] = "application/json"
            headers["Authorization"] = extras.getString("auth")
            intent.putExtras(extras)

            val jsonArr = JSONArrayRequest("$url/domains", Request.Method.GET, headers, null, "Unable to get domains.", rspAction)
            jsonArr.tag = TAG
            vs.addToRequestQueue(jsonArr)
        }
    }

}

