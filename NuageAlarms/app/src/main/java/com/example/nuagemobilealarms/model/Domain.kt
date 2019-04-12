package com.example.nuagemobilealarms.model

data class Domain(
    override val id: String,
    override val parentid: String,
    override val name: String,
    override val childList: ArrayList<Zone>
) : Entity(id = id, parentid = parentid, name = name, childList = childList)
