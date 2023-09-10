package com.example.authenticationservice.sample

import com.example.authenticationservice.application.web.controller.dto.request.CreateEventRequest
import java.sql.Time
import java.time.LocalDate

object RegisterEventSample {
    fun getRegisterEventRequest(): com.example.authenticationservice.application.web.controller.dto.request.CreateEventRequest =
        com.example.authenticationservice.application.web.controller.dto.request.CreateEventRequest(
            name = "Sample Event",
            aboutEvent = "This is a sample event description.",
            cep = "12345-678",
            number = 10,
            complement = "Sample complement",
            eventDate = LocalDate.now().plusDays(1),
            startHour = Time.valueOf("09:00:00"),
            durationHours = 2,
            imageUrl = "https://example.com/image.jpg"
        )
}