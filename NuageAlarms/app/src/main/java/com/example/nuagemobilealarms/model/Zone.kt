package com.example.nuagemobilealarms.model

data class Zone(
    override val id: String,
    override val parentid: String,
    override val name: String,
    override val childList: ArrayList<VPort> = arrayListOf()
) : Entity(id = id, parentid = parentid, name = name, type = Type.ZONE, childList = childList) {
    fun storeVPorts(ls: List<VPort>) {
        ls.forEach { if (it.parentid == this.id) this.childList.add(it) }
    }
}