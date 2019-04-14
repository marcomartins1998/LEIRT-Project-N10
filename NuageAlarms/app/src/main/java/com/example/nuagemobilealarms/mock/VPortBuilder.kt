package com.example.nuagemobilealarms.mock

import com.example.nuagemobilealarms.model.VPort

class VPortBuilder {
    companion object {
        fun createVPort(initialId: Int, parentId: Int, nChilds: Int): VPort {
            return VPort(initialId.toString(), parentId.toString(), "VPort $initialId")
        }
    }
}