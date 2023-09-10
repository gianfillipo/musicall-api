package com.example.authenticationservice.application.web.dto.request

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate
import javax.validation.constraints.Future

data class FilterMusicianRequest (
    @JsonProperty("date") @field:DateTimeFormat(pattern = "yyyy/MM/dd") @field:Future(message = "Event date must be in the future") val date: LocalDate?,
    @JsonProperty("cep") val cep: String?,
    @JsonProperty("instrumentsId") val instrumentsId: List<Long>?
)
