package com.example.nuagemobilealarms.model

import java.util.*

data class Alarm(
    val id: String,
    val parentid: String,
    val parenttype: String,
    val name: String,
    val reason: String,
    val severity: String,
    val startDate: Date? = null
)