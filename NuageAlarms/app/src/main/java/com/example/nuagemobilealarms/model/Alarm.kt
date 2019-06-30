package com.example.nuagemobilealarms.model

import java.util.*

data class Alarm(
    val id: String,
    val parentid: String,
    val parenttype: String,
    val name: String,
    val reason: String,
    val severity: String,
    val startDate: Date? = null,
    val acknowledged: Boolean
){
    companion object {
        fun mapToAlarm(map: Map<String, String>, parenttype: String): Alarm{
            return Alarm(id = map["ID"]!!,
                parentid = map["parentID"]!!,
                parenttype = parenttype,
                name = map["name"]!!,
                reason = map["reason"]!!,
                severity = map["severity"]!!,
                startDate = Date(map["timestamp"]!!.toLong()),
                acknowledged = map["acknowledged"]!!.toBoolean()
            )
        }
    }
}