package com.example.authenticationservice.application.web.dto.response

import com.example.authenticationservice.domain.entities.EventJob
import java.sql.Time
import java.time.LocalDate
import java.util.*

data class EventsInfoResponse (
    val name: String,
    val aboutEvent: String,
    val cep: String,
    val number: Int,
    val complement: String,
    val eventDate: LocalDate,
    val startHour: Date,
    val durationHours : Int,
    val imageUrl : String
){
    lateinit var eventJob: EventJobDto
}
