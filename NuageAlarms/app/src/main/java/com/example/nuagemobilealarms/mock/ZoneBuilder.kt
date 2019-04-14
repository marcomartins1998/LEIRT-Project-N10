package com.example.nuagemobilealarms.mock

import com.example.nuagemobilealarms.model.Zone

class ZoneBuilder {
    companion object {
        fun createZone(initialId: Int, parentId: Int, nChilds: Int): Zone {
            val vportList = IntRange(1, nChilds).map {
                VPortBuilder.createVPort(
                    (initialId.toString() + it.toString()).toInt(),
                    initialId,
                    nChilds
                )
            }
            return Zone(initialId.toString(), parentId.toString(), "Zone $initialId", ArrayList(vportList))
        }
    }
}