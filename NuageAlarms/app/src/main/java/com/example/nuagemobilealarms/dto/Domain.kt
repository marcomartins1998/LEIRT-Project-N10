package com.example.nuagemobilealarms.dto

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
data class Domain(@JsonProperty("ID") val id: String,
                  @JsonProperty("name") val name: String,
                  @JsonProperty("description") val description: String?
)
