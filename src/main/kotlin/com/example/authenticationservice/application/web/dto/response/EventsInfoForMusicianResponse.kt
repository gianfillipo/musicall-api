package com.example.authenticationservice.application.web.dto

import java.sql.Time
import java.time.LocalDate

class EventsInfoForMusician (
    val idEvento: Long,
    val name: String,
    val data: LocalDate,
    val starHour: Time,
    val distance: Float,
    val vaga: String
) {
}
