package com.example.authenticationservice.application.web.dto.response

import java.sql.Time
import java.time.LocalDate
import java.util.*

class EventsInfoForMusicianResponse (
    val idEvento: Long,
    val name: String,
    val data: LocalDate,
    val hour: Date,
    val vaga: String
) {
    var distance: Float = 0.0f
}
