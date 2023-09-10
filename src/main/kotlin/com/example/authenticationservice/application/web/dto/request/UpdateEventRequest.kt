package com.example.authenticationservice.application.web.dto.request

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.format.annotation.DateTimeFormat
import java.sql.Time
import java.time.LocalDate
import javax.validation.constraints.*

data class UpdateEventRequest(
        @JsonProperty("id") @field:NotNull(message = "O ID não pode ser nulo") val id: Long?,
        @JsonProperty("name") val name: String?,
        @JsonProperty("aboutEvent") val aboutEvent: String?,
        @JsonProperty("cep")  val cep: String?,
        @JsonProperty("number") val number: Int,
        @JsonProperty("complement")  val complement: String?,
        @JsonProperty("eventDate") @field:DateTimeFormat(pattern = "yyyy/MM/dd") @field:Future(message = "A data do evento deve estar no futuro") val eventDate: LocalDate?,
        @JsonProperty("startHour") @field:NotNull(message = "A hora de início não pode ser nula") @field:DateTimeFormat(pattern = "HH:mm") val startHour: Time?,
        @JsonProperty("durationHours") @field:Positive(message = "A duração do evento deve ser um valor positivo") val durationHours: Int?,
        @JsonProperty("imageUrl") @field:NotNull(message = "A URL da imagem não pode ser nula") val imageUrl: String?
)
