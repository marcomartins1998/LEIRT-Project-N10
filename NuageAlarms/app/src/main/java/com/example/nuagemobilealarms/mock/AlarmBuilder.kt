package com.example.nuagemobilealarms.mock

import com.example.nuagemobilealarms.dto.EntityDto
import com.example.nuagemobilealarms.model.Alarm
import java.util.*

class AlarmBuilder {
    companion object {
        fun mockAlarm(entityList: List<EntityDto>): Alarm {
            val rand = Random()
            val parent = entityList[rand.nextInt(entityList.size)]
            val severityArr = arrayOf("critical", "major", "minor", "warning", "info")
            return Alarm(
                id = rand.nextInt(65536).toString(),
                parentid = parent.parentid.toString(),
                parenttype = parent.type.name,
                name = "Test Name",
                reason = "Test Reason: Very bad reason.",
                severity = severityArr[rand.nextInt(severityArr.size)],
                startDate = Date(),
                acknowledged = false
            )
        }
    }
}