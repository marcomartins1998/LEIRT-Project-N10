package com.example.nuagemobilealarms.helper

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.example.nuagemobilealarms.dto.Properties
import com.example.nuagemobilealarms.model.Alarm
import com.example.nuagemobilealarms.model.Filters
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.io.*

const val SUBSCRIPTION_FILENAME = "currsubscription.txt"
const val PROPERTIES_FILENAME = "properties.txt"
//const val ENITITY_LIST_FILENAME_SUFIX = "entitylist.txt"
const val FILTERS_FILENAME_SUFIX = "filters.txt"
const val ALARM_LOG_FILENAME_SUFIX = "alarmlog.txt"

class FileHelper(val context: Context){

    fun read(filename: String, defaultReturn: String?): String? {
        var fileis: FileInputStream? = null
        var str: String
        try {
            fileis = context.openFileInput(filename)
            val br = BufferedReader(InputStreamReader(fileis))
            str = br.readText()
            //br.read(sb)
        } catch (e: IOException) {
            return defaultReturn
        } finally {
            fileis?.close()
        }
        return if (str == "") defaultReturn else str
    }

    @Synchronized
    fun write(filename: String, input: String){
        var fileos: FileOutputStream? = null
        try {
            fileos = context.openFileOutput(filename, MODE_PRIVATE)
            fileos.write(input.toByteArray())
        } catch (e: IOException){
            AndroidHelper.toastMessage(context, e.message)
        } finally {
            fileos?.close()
        }
    }

    fun getProperties(): Properties {
        val mapper = ObjectMapper()
        val jsonString = read(PROPERTIES_FILENAME, "{}")
        return mapper.readValue(jsonString, Properties::class.java)
    }

    fun putProperties(properties: Properties){
        val mapper = ObjectMapper()
        println("CHECK:" + mapper.writeValueAsString(properties))
        write(PROPERTIES_FILENAME, mapper.writeValueAsString(properties))
    }

    fun getFilters(ip: String, username: String): Filters? {
        val mapper = jacksonObjectMapper()
        val jsonString = read("$ip-$username-$FILTERS_FILENAME_SUFIX", null)
        if (jsonString == null) return null
        else return mapper.readValue(jsonString, Filters::class.java)
    }

    fun putFilters(ip: String, username: String, filters: Filters) {
        val mapper = ObjectMapper()
        println("CHECK:" + mapper.writeValueAsString(filters))
        write("$ip-$username-$FILTERS_FILENAME_SUFIX", mapper.writeValueAsString(filters))
    }

    /*fun getEntityList(ip: String, username: String): ArrayList<EntityDto> {
        val mapper = jacksonObjectMapper()
        val jsonString = read("$ip-$username-$ENITITY_LIST_FILENAME_SUFIX", "[]")
        val entityList: ArrayList<EntityDto> = mapper.readValue(jsonString)
        return entityList
    }

    fun putEntityList(entityList: ArrayList<EntityDto>, ip: String, username: String) {
        val mapper = ObjectMapper()
        println("CHECK:" + mapper.writeValueAsString(entityList))
        write("$ip-$username-$ENITITY_LIST_FILENAME_SUFIX", mapper.writeValueAsString(entityList))
    }*/

    fun getAlarmList(ip: String, username: String): ArrayList<Alarm> {
        val mapper = jacksonObjectMapper()
        val jsonString = read("$ip-$username-$ALARM_LOG_FILENAME_SUFIX", "[]")
        val alarmList: ArrayList<Alarm> = mapper.readValue(jsonString!!)
        return alarmList
    }

    fun putAlarmList(alarmList: ArrayList<Alarm>, ip: String, username: String) {
        val mapper = ObjectMapper()
        println("CHECK:" + mapper.writeValueAsString(alarmList))
        write("$ip-$username-$ALARM_LOG_FILENAME_SUFIX", mapper.writeValueAsString(alarmList))
    }

    fun addToAlarmList(alarm: Alarm, ip: String, username: String) {
        val mapper = jacksonObjectMapper()
        val jsonString = read("$ip-$username-$ALARM_LOG_FILENAME_SUFIX", "[]")
        val alarmList: ArrayList<Alarm> = mapper.readValue(jsonString!!)
        alarmList.add(alarm)
        write("$ip-$username-$ALARM_LOG_FILENAME_SUFIX", mapper.writeValueAsString(alarmList))
    }

    fun getCurrentSubscription(): String = read(SUBSCRIPTION_FILENAME, "")!!

    fun putCurrentSubscription(subscription: String) {
        write(SUBSCRIPTION_FILENAME, subscription)
    }
}