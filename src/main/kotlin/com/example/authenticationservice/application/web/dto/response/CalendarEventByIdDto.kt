package com.example.authenticationservice.application.web.dto.response

import com.example.authenticationservice.domain.entities.Event
import java.sql.Time
import java.time.LocalDate

class CalendarEventByIdDto(
    val id: Long,
    val name: String,
    val eventDate: LocalDate,
    val vagas: List<EventJobDto>,
    val cep: String,
    val numero: Int,
    val complemento :String,
    val horaioInicio: Time,
    val duracaoEvento: Int
) {

    constructor(event: Event) : this(
        id = event.id,
        name = event.name,
        eventDate = event.eventDate,
        vagas = event.eventJob.map { EventJobDto(it) },
        cep = event.cep,
        numero= event.number,
        complemento = event.complement,
        horaioInicio = event.startHour,
        duracaoEvento = event.durationHours
    )
}
