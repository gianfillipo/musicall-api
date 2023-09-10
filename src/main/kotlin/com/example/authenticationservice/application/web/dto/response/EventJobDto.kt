package com.example.authenticationservice.application.web.dto.response

import com.example.authenticationservice.domain.entities.EventJob

data class EventJobDto (
        val id: Long,
        val fkEvento : Long,
        val instrumentName : String,
        val payment : Double?,
        val isAvailable: Boolean?
) {
    constructor(eventJob: EventJob): this(
            id = eventJob.id,
            fkEvento = eventJob.event.id,
            instrumentName = eventJob.instrument.name,
            payment = eventJob.payment,
            isAvailable = eventJob.musician == null
    )

        constructor(): this(
                id = 0,
                fkEvento = 0,
                instrumentName = "",
                payment = 0.0,
                isAvailable = false
        )
}
