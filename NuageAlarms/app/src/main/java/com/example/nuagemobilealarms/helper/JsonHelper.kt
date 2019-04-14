package com.example.nuagemobilealarms.helper

import android.content.Context
import android.util.Log
import com.example.nuagemobilealarms.dto.DomainDto
import com.example.nuagemobilealarms.dto.EnterpriseDto
import com.example.nuagemobilealarms.dto.VPortDto
import com.example.nuagemobilealarms.dto.ZoneDto
import com.example.nuagemobilealarms.model.Domain
import com.example.nuagemobilealarms.model.Enterprise
import com.example.nuagemobilealarms.model.VPort
import com.example.nuagemobilealarms.model.Zone
import com.fasterxml.jackson.databind.ObjectMapper
import java8.util.concurrent.CompletableFuture
import org.json.JSONArray
import org.json.JSONObject

class JsonHelper {
    companion object {
        val TAG = "JsonHelper"
        fun JSONArray.forEach(t: (JSONObject) -> Unit) {
            var index = 0
            while (index < this.length()) {
                t(this.optJSONObject(index))
                index++
            }
        }

        fun convertToEnterpriseList(context: Context, jsonarr: JSONArray?, cp: CompletableFuture<List<Enterprise>>) {
            val mapper = ObjectMapper()
            try {
                val enterpriseDtoList: List<EnterpriseDto> = mapper.readValue(
                    jsonarr.toString(),
                    mapper.typeFactory.constructCollectionType(List::class.java, EnterpriseDto::class.java)
                )
                cp.complete(enterpriseDtoList.map(EnterpriseDto::toModel))
                Log.d(TAG, cp.toString())
            } catch (e: Exception) {
                cp.complete(arrayListOf())
                Log.e(TAG, e.message)
            }
        }

        fun convertToDomainList(context: Context, jsonarr: JSONArray?, cp: CompletableFuture<List<Domain>>) {
            val mapper = ObjectMapper()
            try {
                val domainDtoList: List<DomainDto> = mapper.readValue(
                    jsonarr.toString(),
                    mapper.typeFactory.constructCollectionType(List::class.java, DomainDto::class.java)
                )
                cp.complete(domainDtoList.map(DomainDto::toModel))
                Log.d(TAG, cp.toString())
            } catch (e: Exception) {
                cp.complete(arrayListOf())
                Log.e(TAG, e.message)
            }
        }

        fun convertToZoneList(context: Context, jsonarr: JSONArray?, cp: CompletableFuture<List<Zone>>) {
            val mapper = ObjectMapper()
            try {
                val zoneDtoList: List<ZoneDto> = mapper.readValue(
                    jsonarr.toString(),
                    mapper.typeFactory.constructCollectionType(List::class.java, ZoneDto::class.java)
                )
                cp.complete(zoneDtoList.map(ZoneDto::toModel))
                Log.d(TAG, cp.toString())
            } catch (e: Exception) {
                cp.complete(arrayListOf())
                Log.e(TAG, e.message)
            }
        }

        fun convertToVPortList(context: Context, jsonarr: JSONArray?, cp: CompletableFuture<List<VPort>>) {
            val mapper = ObjectMapper()
            try {
                val vportDtoList: List<VPortDto> = mapper.readValue(
                    jsonarr.toString(),
                    mapper.typeFactory.constructCollectionType(List::class.java, VPortDto::class.java)
                )
                cp.complete(vportDtoList.map(VPortDto::toModel))
                Log.d(TAG, cp.toString())
            } catch (e: Exception) {
                cp.complete(arrayListOf())
                Log.e(TAG, e.message)
            }
        }
    }
}