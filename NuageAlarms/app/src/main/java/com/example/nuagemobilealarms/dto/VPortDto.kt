package com.example.nuagemobilealarms.dto

import com.example.nuagemobilealarms.model.VPort
import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
data class VPortDto(
    @JsonProperty("ID") val id: String,
    @JsonProperty("zoneID") val zid: String,
    @JsonProperty("name") val name: String
) {
    fun toModel() = VPort(id = id, parentid = zid, name = name)
}