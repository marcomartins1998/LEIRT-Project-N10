package com.example.nuagemobilealarms.mock

import com.example.nuagemobilealarms.model.Enterprise

class EnterpriseBuilder {
    companion object {
        fun createEnterprise(initialId: Int, nChilds: Int): Enterprise {
            val domainList = IntRange(1, nChilds).map {
                DomainBuilder.createDomain(
                    (initialId.toString() + it.toString()).toInt(),
                    initialId,
                    nChilds
                )
            }
            return Enterprise(initialId.toString(), "Enterprise $initialId", ArrayList(domainList))
        }
    }
}