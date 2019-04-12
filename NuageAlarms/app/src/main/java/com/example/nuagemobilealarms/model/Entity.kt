package com.example.nuagemobilealarms.model

abstract class Entity(
    open val id: String,
    open val parentid: String? = null,
    open val name: String,
    open val childList: List<Entity>? = null,
    var checked: Boolean = true
) {
    fun propagateChecked() = childList?.forEach { it.checked = this.checked }

    fun propagateChecked(list: ArrayList<Entity>?) {
        list?.forEach {
            if (it.parentid == this.id) it.checked = this.checked
        }
    }
}