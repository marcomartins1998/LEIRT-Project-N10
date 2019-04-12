package com.example.nuagemobilealarms.dto

import com.example.nuagemobilealarms.model.Zone
import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
data class ZoneDto(
    @JsonProperty("ID") val id: String,
    @JsonProperty("parentID") val did: String,
    val name: String
) {
    fun toModel() = Zone(id = id, parentid = did, name = name, childList = arrayListOf())
}