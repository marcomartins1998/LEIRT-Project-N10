package com.example.nuagemobilealarms.model

data class Alarm(
    val id: String,
    val parentid: String,
    val parentType: String,
    val name: String,
    val reason: String,
    val severity: String
)