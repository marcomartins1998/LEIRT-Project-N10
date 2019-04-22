package com.example.nuagemobilealarms.connect

import android.content.Context
import android.content.Intent
import android.util.Base64
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.example.nuagemobilealarms.helper.AndroidHelper
import com.example.nuagemobilealarms.helper.JsonHelper
import com.example.nuagemobilealarms.model.*
import java8.util.concurrent.CompletableFuture
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import kotlin.text.Charsets.UTF_8


//TODO try to clean repeated code
class VolleyHelper(val context: Context, val intent: Intent, val vs: VolleySingleton){
    val TAG = "VolleyHelper"

    fun JSONArrayRequest(url: String, method: Int, headers: HashMap<String,String>, requestBody: JSONObject?, errorMsg: String,
                         rspAction: (JSONArray?) -> Unit): JsonArrayRequest{
        return object: JsonArrayRequest(
            method,
            url,
            null,
            Response.Listener(rspAction),
            Response.ErrorListener {
                rspAction(JSONArray())
                Log.e(TAG, "${it.message}\n$errorMsg")
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
                rspAction(JSONObject())
                Log.e(TAG, "${it.message}\n$errorMsg")
            }
        ){
            override fun getHeaders(): MutableMap<String, String> = headers
            override fun getBody(): ByteArray? = requestBody?.toString()?.toByteArray(UTF_8)
        }
    }

    fun NuageVersionRequest(url: String, tag: String, rspAction: (JSONObject?) -> Unit) {
        val headers = HashMap<String, String>()
        headers["Content-type"] = "application/json"
        val jsonArr = JSONObjectRequest(url, Request.Method.GET, headers, null, "Invalid parameters/Try again later.") {
            //TODO adicionar esta verificação nos pedidos http
            if (it?.length() == 0) AndroidHelper.toastMessage(context, "Internet connection needed.")
            else rspAction(it)
        }
        jsonArr.tag = tag
        vs.addToRequestQueue(jsonArr)
    }

    fun NuageAuthRequest(url: String, tag: String, rspAction: (JSONArray?) -> Unit) {
        val extras = intent.extras!!
        val headers = HashMap<String, String>()
        headers["X-Nuage-Organization"] = extras.getString("companyname")
        headers["Content-type"] = "application/json"
        //headers["Authorization"] = "Basic "+ extras.getString("initauth")
        headers["Authorization"] = "Basic " + Base64.encodeToString(
            "${extras.getString("username")}:${extras.getString("password")}".toByteArray(),
            Base64.DEFAULT
        )
        val jsonArr = JSONArrayRequest("$url/me", Request.Method.GET, headers, null, "Login fail, please try again.", rspAction)
        jsonArr.tag = tag
        vs.addToRequestQueue(jsonArr)
    }

    //TODO testar este método
    @Synchronized
    fun NuageAuthIfExpired(url: String, tag: String, action: () -> Unit) {
        val extras = intent.extras!!
        if(Calendar.getInstance().time.time > extras.getLong("authexpiry")){
            NuageAuthRequest(url, tag) {
                val apiKey = it?.getJSONObject(0)?.getString("APIKey")
                val apiKeyExpiry = it?.getJSONObject(0)?.getLong("APIKeyExpiry")
                intent.putExtra("auth", "Basic "+ Base64.encodeToString("${extras.getString("username")!!.trim()}:$apiKey".toByteArray(), 0))
                intent.putExtra("authexpiry", apiKeyExpiry)
                action()
            }
        } else action()
    }

    fun NuageGetEnterprises(tag: String): CompletableFuture<List<Enterprise>> {
        val cp: CompletableFuture<List<Enterprise>> = CompletableFuture()
        val url = intent.extras!!.getString("url")!!
        //Thread(Runnable {
            NuageAuthIfExpired(url, tag) {
                val extras = intent.extras!!
                val headers = HashMap<String, String>()
                headers["X-Nuage-Organization"] = extras.getString("companyname") ?: ""
                headers["Content-type"] = "application/json"
                headers["Authorization"] = extras.getString("auth") ?: ""

                val jsonArr = JSONArrayRequest(
                    "$url/enterprises",
                    Request.Method.GET,
                    headers,
                    null,
                    "Unable to get enterprises."
                ) {
                    JsonHelper.convertToEnterpriseList(context, it, cp)
                }
                jsonArr.tag = tag
                vs.addToRequestQueue(jsonArr)
            }
        //}).start()
        return cp
    }

    fun NuageGetDomains(tag: String): CompletableFuture<List<Domain>> {
        val cp: CompletableFuture<List<Domain>> = CompletableFuture()
        val url = intent.extras!!.getString("url")!!
        //Thread(Runnable {
            NuageAuthIfExpired(url, tag) {
                val extras = intent.extras!!
                val headers = HashMap<String, String>()
                headers["X-Nuage-Organization"] = extras.getString("companyname") ?: ""
                headers["Content-type"] = "application/json"
                headers["Authorization"] = extras.getString("auth") ?: ""

                val jsonArr =
                    JSONArrayRequest("$url/domains", Request.Method.GET, headers, null, "Unable to get domains.") {
                        JsonHelper.convertToDomainList(context, it, cp)
                }
                jsonArr.tag = tag
                vs.addToRequestQueue(jsonArr)
            }
        //}).start()
        return cp
    }

    fun NuageGetZones(tag: String): CompletableFuture<List<Zone>> {
        val cp: CompletableFuture<List<Zone>> = CompletableFuture()
        val url = intent.extras!!.getString("url")!!
        //Thread(Runnable {
            NuageAuthIfExpired(url, tag) {
                val extras = intent.extras!!
                val headers = HashMap<String, String>()
                headers["X-Nuage-Organization"] = extras.getString("companyname") ?: ""
                headers["Content-type"] = "application/json"
                headers["Authorization"] = extras.getString("auth") ?: ""

                val jsonArr =
                    JSONArrayRequest("$url/zones", Request.Method.GET, headers, null, "Unable to get zones.") {
                        JsonHelper.convertToZoneList(context, it, cp)
                    }
                jsonArr.tag = tag
                vs.addToRequestQueue(jsonArr)
            }
        //}).start()
        return cp
    }

    fun NuageGetVPorts(tag: String, domainid: String): CompletableFuture<List<VPort>> {
        val cp: CompletableFuture<List<VPort>> = CompletableFuture()
        val url = intent.extras!!.getString("url")!!
        //Thread(Runnable {
            NuageAuthIfExpired(url, tag) {
                val extras = intent.extras!!
                val headers = HashMap<String, String>()
                headers["X-Nuage-Organization"] = extras.getString("companyname") ?: ""
                headers["Content-type"] = "application/json"
                headers["Authorization"] = extras.getString("auth") ?: ""

                val jsonArr = JSONArrayRequest(
                    "$url/domains/$domainid/vports",
                    Request.Method.GET,
                    headers,
                    null,
                    "Unable to get vports."
                ) {
                    JsonHelper.convertToVPortList(context, it, cp)
                }
                jsonArr.tag = tag
                vs.addToRequestQueue(jsonArr)
            }
        //}).start()
        return cp
    }

    fun NuageGetAllEnterpriseAlarms(tag: String, enterpriseid: String): CompletableFuture<List<Alarm>> {
        val cp: CompletableFuture<List<Alarm>> = CompletableFuture()
        val url = intent.extras!!.getString("url")!!
        //Thread(Runnable {
        NuageAuthIfExpired(url, tag) {
            val extras = intent.extras!!
            val headers = HashMap<String, String>()
            headers["X-Nuage-Organization"] = extras.getString("companyname") ?: ""
            headers["Content-type"] = "application/json"
            headers["Authorization"] = extras.getString("auth") ?: ""

            val jsonArr = JSONArrayRequest(
                "$url/enterprises/$enterpriseid/allalarms",
                Request.Method.GET,
                headers,
                null,
                "Unable to get enterprise alarms."
            ) {
                JsonHelper.convertToAlarmList(context, it, cp)
            }
            jsonArr.tag = tag
            vs.addToRequestQueue(jsonArr)
        }
        //}).start()
        return cp
    }

}

