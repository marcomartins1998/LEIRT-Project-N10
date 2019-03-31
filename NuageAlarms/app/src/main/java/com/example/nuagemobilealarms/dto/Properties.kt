package com.example.nuagemobilealarms.dto

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
data class Properties(@JsonProperty("servername") var servername: String? = null,
                      @JsonProperty("url") var url: String? = null,
                      @JsonProperty("companyname") var companyname: String? = null,
                      @JsonProperty("username") var username: String? = null,
                      @JsonProperty("initauth") var initauth: String? = null,
                      @JsonProperty("auth") var auth: String? = null,
                      @JsonProperty("authexpiry") var authexpiry: String? = null
)
