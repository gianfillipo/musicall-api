package com.example.authenticationservice.application.web.dto.response

import com.example.authenticationservice.domain.entities.Event
import java.sql.Time
import java.time.LocalDate

data class EventDto(
        val id: Long,
        val name: String,
        var cep: String,
        val number: Int,
        val eventDate: LocalDate,
        val startHour: Time,
        val durationHours: Int,
        val imageUrl: String,
        val eventJobs: List<com.example.authenticationservice.application.web.controller.dto.response.EventJobDto>
) {
        var distance: Int = Int.MAX_VALUE

    constructor(event: Event) : this(
            id = event.id,
            name = event.name,
            cep = event.cep,
            number = event.number,
            eventDate = event.eventDate,
            startHour = event.startHour,
            durationHours = event.durationHours,
            imageUrl = event.imageUrl,
            eventJobs = event.eventJob.map {
                    com.example.authenticationservice.application.web.controller.dto.response.EventJobDto(
                            it
                    )
            }
    )
}
