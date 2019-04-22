package com.example.nuagemobilealarms.dto

import com.example.nuagemobilealarms.model.Alarm
import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
data class AlarmDto(
    @JsonProperty("ID") val id: String,
    @JsonProperty("parentID") val parentid: String,
    @JsonProperty("parentType") val parentType: String,
    @JsonProperty("name") val name: String,
    @JsonProperty("reason") val reason: String,
    @JsonProperty("severity") val severity: String
) {
    fun toModel() =
        Alarm(id = id, parentid = parentid, parentType = parentType, name = name, reason = reason, severity = severity)
}