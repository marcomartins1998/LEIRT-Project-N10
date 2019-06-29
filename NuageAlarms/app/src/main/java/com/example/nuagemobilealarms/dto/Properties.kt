package com.example.nuagemobilealarms.dto

import android.widget.EditText
import android.widget.TextView
import com.fasterxml.jackson.annotation.JsonAutoDetect

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
data class Properties(
    val servernameip: String? = null,
    val port: String? = null,
    val companyname: String? = null,
    val username: String? = null
) {
    fun replace(
            servernameip: String? = this.servernameip,
            port: String? = this.port,
            companyname: String? = this.companyname,
            username: String? = this.username
    ) = Properties(
            servernameip = servernameip,
            port = port,
            companyname = companyname,
            username = username
    )

    fun assignToMain(et1: EditText, et2: EditText) {
        et1.setText(servernameip, TextView.BufferType.EDITABLE)
        et2.setText(port, TextView.BufferType.EDITABLE)
    }

    fun assignToLogin(et1: EditText, et2: EditText) {
        et1.setText(companyname, TextView.BufferType.EDITABLE)
        et2.setText(username, TextView.BufferType.EDITABLE)
    }

    fun noneNullOrEmpty() =
        !(servernameip.isNullOrEmpty() || port.isNullOrEmpty() || companyname.isNullOrEmpty() || username.isNullOrEmpty())
}
