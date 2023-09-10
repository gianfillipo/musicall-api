package com.example.authenticationservice.application.web.dto.response

import com.example.authenticationservice.domain.entities.Event
import java.sql.Time
import java.time.LocalDate

data class CreateEventDto (
        val id: Long,
        val name: String,
        val aboutEvent: String,
        val cep: String,
        val number: Int,
        val eventDate: LocalDate,
        val startHour: Time,
        val durationHours: Int
) {
    constructor(): this (
            id = 0,
            name = "",
            aboutEvent = "",
            cep = "",
            number = 0,
            eventDate = LocalDate.now(),
            startHour = Time.valueOf("00:00:00"),
            durationHours = 0
    )
    constructor(event: Event): this(
            id = event.id,
            name = event.name,
            aboutEvent = event.aboutEvent,
            cep = event.cep,
            number = event.number,
            eventDate = event.eventDate,
            startHour = event.startHour,
            durationHours = event.durationHours
    )
}