package com.example.authenticationservice.application.web.dto.response

import com.example.authenticationservice.domain.entities.Event
import java.time.LocalDate

data class CalendarEventDto(
        val id: Long,
        val name: String,
        val eventDate: LocalDate
) {

    constructor(event: Event) : this(
            id = event.id,
            name = event.name,
            eventDate = event.eventDate
    )
}
