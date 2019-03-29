package com.example.nuagemobilealarms.helper

import org.json.JSONArray
import org.json.JSONObject

class JsonHelper {
    fun JSONArray.forEach(t: (JSONObject) -> Unit) {
        var index = 0
        while(index < this.length()){
            t(this.optJSONObject(index))
            index++
        }
    }
}