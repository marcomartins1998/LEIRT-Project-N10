package com.example.nuagemobilealarms.model

data class Enterprise(
    override val id: String,
    override val name: String,
    override val childList: ArrayList<Domain> = arrayListOf()
) : Entity(id = id, name = name, type = Type.ENTERPRISE, childList = childList) {
    fun storeDomains(ls: List<Domain>) {
        ls.forEach { if (it.parentid == this.id) this.childList.add(it) }
    }
}