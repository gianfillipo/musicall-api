package com.example.authenticationservice.application.web.dto.response

data class EventJobPerInstrumentResponse (
    val instrumentName: String,
    val totalVacancies: Long
) {
}