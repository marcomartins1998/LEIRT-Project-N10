package com.example.nuagemobilealarms.model

data class Domain(
    override val id: String,
    override val parentid: String,
    override val name: String,
    override val childList: ArrayList<Zone> = arrayListOf()
) : Entity(id = id, parentid = parentid, name = name, type = Type.DOMAIN, childList = childList) {
    fun storeZones(ls: List<Zone>) {
        ls.forEach { if (it.parentid == this.id) this.childList.add(it) }
    }
}
