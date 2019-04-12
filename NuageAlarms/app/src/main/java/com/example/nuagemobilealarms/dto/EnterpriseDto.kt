package com.example.nuagemobilealarms.dto

import com.example.nuagemobilealarms.model.Enterprise
import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
data class EnterpriseDto(
    @JsonProperty("ID") val id: String,
    val name: String
) {
    fun toModel() = Enterprise(id = id, name = name, childList = arrayListOf())
}