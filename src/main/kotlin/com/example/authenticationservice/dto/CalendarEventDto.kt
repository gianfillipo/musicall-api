package com.example.authenticationservice.dto

import com.example.authenticationservice.model.Event
import com.example.authenticationservice.model.EventJob
import com.example.authenticationservice.model.User
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.format.annotation.DateTimeFormat
import java.sql.Time
import java.time.LocalDate
import javax.validation.constraints.Future
import javax.validation.constraints.NotBlank

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
