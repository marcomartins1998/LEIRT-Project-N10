package com.example.nuagemobilealarms.model

import com.example.nuagemobilealarms.dto.EntityDto

data class Filters(
    val entityList: ArrayList<EntityDto>?,
    val severity: String?
) {
}