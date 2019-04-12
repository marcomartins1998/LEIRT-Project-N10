package com.example.nuagemobilealarms.model

data class Enterprise(
    override val id: String,
    override val name: String,
    override val childList: ArrayList<Domain>
) : Entity(id = id, name = name, childList = childList)