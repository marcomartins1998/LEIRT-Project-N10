package com.example.nuagemobilealarms.helper

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.example.nuagemobilealarms.dto.Properties
import com.fasterxml.jackson.databind.ObjectMapper
import java.io.*

const val PROPERTIES_FILENAME = "properties.txt"

class FileHelper(val context: Context){

    fun read(filename: String): String{
        var fileis: FileInputStream? = null
        var str: String
        try {
            fileis = context.openFileInput(filename)
            val br = BufferedReader(InputStreamReader(fileis))
            str = br.readText()
            //br.read(sb)
        } catch (e: IOException) {
            return "{}"
        } finally {
            fileis?.close()
        }
        println("READ CHECK:" + str)
        return if (str == "") "{}" else str
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

    fun getProperties(): Properties{
        val mapper = ObjectMapper()
        val jsonString = read(PROPERTIES_FILENAME)
        return mapper.readValue(jsonString, Properties::class.java)
    }

    fun putProperties(properties: Properties){
        val mapper = ObjectMapper()
        println("CHECK:" + mapper.writeValueAsString(properties))
        write(PROPERTIES_FILENAME, mapper.writeValueAsString(properties))
    }
}