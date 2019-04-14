package com.example.nuagemobilealarms.dto

import com.example.nuagemobilealarms.model.Domain
import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
data class DomainDto(
    @JsonProperty("ID") val id: String,
    @JsonProperty("parentID") val eid: String,
    @JsonProperty("name") val name: String
) {
    fun toModel() = Domain(id = id, parentid = eid, name = name, childList = arrayListOf())
}
