package com.example.nuagemobilealarms.dto

import com.example.nuagemobilealarms.model.Type
import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
data class EntityDto(
    @JsonProperty("id") val id: String,
    @JsonProperty("parentid") val parentid: String? = null,
    @JsonProperty("name") val name: String,
    @JsonProperty("type") val type: Type,
    @JsonProperty("checked") var checked: Boolean = true
)