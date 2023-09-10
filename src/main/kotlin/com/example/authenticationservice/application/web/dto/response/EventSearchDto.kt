package com.example.authenticationservice.application.web.dto.response

import java.util.Date
import java.time.LocalDate

data class EventSearchDto (
    val id:Long,
    val imageUrl:String,
    val eventDate:LocalDate,
    val startHour: Date,
    var cep:String,
    var distance: Int = 0
) {

    constructor() : this(
        id = 0,
        imageUrl = "",
        eventDate = LocalDate.now(),
        startHour = Date(0),
        cep = "",
        distance = 0
    )

    constructor(id: Long, imageUrl: String, eventDate: LocalDate, startHour: Date, cep: String) : this(
        id = id,
        imageUrl = imageUrl,
        eventDate = eventDate,
        startHour = startHour,
        cep = cep,
        distance = 0
    )
}