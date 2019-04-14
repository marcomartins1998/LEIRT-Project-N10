package com.example.nuagemobilealarms.mock

import com.example.nuagemobilealarms.model.Domain

class DomainBuilder {
    companion object {
        fun createDomain(initialId: Int, parentId: Int, nChilds: Int): Domain {
            val zoneList = IntRange(1, nChilds).map {
                ZoneBuilder.createZone(
                    (initialId.toString() + it.toString()).toInt(),
                    initialId,
                    nChilds
                )
            }
            return Domain(initialId.toString(), parentId.toString(), "Domain $initialId", ArrayList(zoneList))
        }
    }
}