package com.example.nuagemobilealarms.helper

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.example.nuagemobilealarms.dto.Properties
import com.fasterxml.jackson.databind.ObjectMapper
import java.io.*

const val PROPERTIES_FILENAME = "properties.txt"

class FileHelper(val context: Context){

    fun read(filename: String): String{
        val sb = charArrayOf()
        try {
            val fileis = context.openFileInput(filename)
            val br = BufferedReader(InputStreamReader(fileis))
            br.read(sb)
        } catch (e: FileNotFoundException){
            AndroidHelper.toastMessage(context, e.message)
        } catch (e: IOException){
            AndroidHelper.toastMessage(context, e.message)
        }
        return if(String(sb) == "") "{}" else String(sb)
    }

    fun write(filename: String, input: String){
        try {
            val fileos = context.openFileOutput(filename, MODE_PRIVATE)
            fileos.write(input.toByteArray())
        } catch (e: FileNotFoundException){
            AndroidHelper.toastMessage(context, e.message)
        } catch (e: IOException){
            AndroidHelper.toastMessage(context, e.message)
        }
    }

    fun getProperties(): Properties{
        val mapper = ObjectMapper()
        val jsonString = read(PROPERTIES_FILENAME)
        println(jsonString)
        return mapper.readValue(jsonString, Properties::class.java)
    }

    fun putProperties(properties: Properties){
        val mapper = ObjectMapper()
        write(PROPERTIES_FILENAME, mapper.writeValueAsString(properties))
    }
}