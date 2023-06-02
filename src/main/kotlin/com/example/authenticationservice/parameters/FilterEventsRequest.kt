package com.example.authenticationservice.parameters

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate
import javax.validation.constraints.*

data class FilterEventsRequest (
    @JsonProperty("date") @field:DateTimeFormat(pattern = "yyyy/MM/dd") @field:Future(message = "Event date must be in the future") val date: LocalDate?,
    @JsonProperty("cep") val cep: String?,
    @JsonProperty("payment") @field:Min(10) val payment: Double?,
    @JsonProperty("instrumentsId") val instrumentsId: List<Long>?
)