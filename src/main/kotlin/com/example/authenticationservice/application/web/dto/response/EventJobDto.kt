package com.example.authenticationservice.application.web.dto.response

import com.example.authenticationservice.domain.entities.EventJob

data class EventJobDto (
    val id: Long,
    val fkEvento : Long,
    val instrumentId : Long,
    val instrumentName: String,
    val payment : Double?,
    val isAvailable: Boolean?
) {
    constructor(eventJob: EventJob): this(
            id = eventJob.id,
            fkEvento = eventJob.event.id,
            instrumentId = eventJob.instrument.id,
            payment = eventJob.payment,
            isAvailable = eventJob.musician == null,
            instrumentName = eventJob.instrument.name
    )

        constructor(): this(
                id = 0,
                fkEvento = 0,
                instrumentId = 0,
                payment = 0.0,
                isAvailable = false,
                instrumentName = ""
        )
}
