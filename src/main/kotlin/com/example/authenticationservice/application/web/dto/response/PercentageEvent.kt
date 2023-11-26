package com.example.authenticationservice.application.web.dto.response

data class PercentageEvent(
    val percentage: Double,
    val type: String
)

data class EventCountProjection(
    val currentMonthCount: Long,
    val lastMonthCount: Long
)

