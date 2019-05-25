package com.example.nuagemobilealarms.helper

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.example.nuagemobilealarms.dto.EntityDto
import com.example.nuagemobilealarms.dto.Properties
import com.example.nuagemobilealarms.model.Alarm
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.io.*

const val PROPERTIES_FILENAME = "properties.txt"
const val ENITITY_LIST_FILENAME = "entitylist.txt"
const val ALARM_LOG = "alarmlog.txt"

class FileHelper(val context: Context){

    fun read(filename: String, defaultReturn: String): String {
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
        println("READ CHECK:" + str)
        return if (str == "") defaultReturn else str
    }

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

    fun getEntityList(): ArrayList<EntityDto> {
        val mapper = jacksonObjectMapper()
        val jsonString = read(ENITITY_LIST_FILENAME, "[]")
        val entityList: ArrayList<EntityDto> = mapper.readValue(jsonString)
        return entityList
    }

    fun putEntityList(entityList: ArrayList<EntityDto>) {
        val mapper = ObjectMapper()
        println("CHECK:" + mapper.writeValueAsString(entityList))
        write(ENITITY_LIST_FILENAME, mapper.writeValueAsString(entityList))
    }

    fun getAlarmList(): ArrayList<Alarm> {
        val mapper = jacksonObjectMapper()
        val jsonString = read(ALARM_LOG, "[]")
        val alarmList: ArrayList<Alarm> = mapper.readValue(jsonString)
        return alarmList
    }

    fun putAlarmList(alarmList: ArrayList<Alarm>) {
        val mapper = ObjectMapper()
        println("CHECK:" + mapper.writeValueAsString(alarmList))
        write(ALARM_LOG, mapper.writeValueAsString(alarmList))
    }

    fun addToAlarmList(alarm: Alarm) {
        val mapper = jacksonObjectMapper()
        val jsonString = read(ALARM_LOG, "[]")
        val alarmList: ArrayList<Alarm> = mapper.readValue(jsonString)
        alarmList.add(alarm)
        write(ALARM_LOG, mapper.writeValueAsString(alarmList))
    }
}