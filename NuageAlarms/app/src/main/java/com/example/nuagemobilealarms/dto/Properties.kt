package com.example.nuagemobilealarms.dto

import android.widget.EditText
import android.widget.TextView
import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
data class Properties(
    @JsonProperty("servernameip") val servernameip: String? = null,
    //@JsonProperty("servername") val servername: String? = null,
    @JsonProperty("port") val port: String? = null,
    //@JsonProperty("url") val url: String? = null,
    @JsonProperty("companyname") val companyname: String? = null,
    @JsonProperty("username") val username: String? = null
    //@JsonProperty("initauth") var initauth: String? = null,
    //@JsonProperty("auth") val auth: String? = null,
    //@JsonProperty("authexpiry") val authexpiry: String? = null
) {
    fun replace(
            servernameip: String? = this.servernameip,
            port: String? = this.port,
            //servername: String? = this.servername,
            //url: String? = this.url,
            companyname: String? = this.companyname,
            username: String? = this.username
            //auth: String? = this.auth,
            //authexpiry: String? = this.authexpiry
    ) = Properties(
            servernameip = servernameip,
            port = port,
            //servername = servername,
            //url = url,
            companyname = companyname,
            username = username
            //auth = auth,
            //authexpiry = authexpiry
    )

    fun assignToMain(et1: EditText, et2: EditText) {
        et1.setText(servernameip, TextView.BufferType.EDITABLE)
        et2.setText(port, TextView.BufferType.EDITABLE)
    }

    fun assignToLogin(et1: EditText, et2: EditText) {
        et1.setText(companyname, TextView.BufferType.EDITABLE)
        et2.setText(username, TextView.BufferType.EDITABLE)
    }
}
