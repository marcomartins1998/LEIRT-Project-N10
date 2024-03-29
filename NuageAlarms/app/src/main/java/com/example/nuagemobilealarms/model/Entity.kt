package com.example.nuagemobilealarms.model

import com.example.nuagemobilealarms.dto.EntityDto

abstract class Entity(
    open val id: String,
    open val parentid: String? = null,
    open val name: String,
    val type: Type,
    open val childList: List<Entity>? = null,
    var checked: Boolean = true
) {
    fun toEntityDto() = EntityDto(id = id, parentid = parentid, name = name, type = type, checked = checked)
    /*fun propagateChecked() = childList?.forEach { it.checked = this.checked }

    fun propagateChecked(list: ArrayList<Entity>?) {
        list?.forEach {
            if (it.parentid == this.id) it.checked = this.checked
        }
    }*/
}