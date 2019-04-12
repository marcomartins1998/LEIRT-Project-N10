package com.example.nuagemobilealarms.model

data class VPort(
    override val id: String,
    override val parentid: String,
    override val name: String
) : Entity(id = id, parentid = parentid, name = name)