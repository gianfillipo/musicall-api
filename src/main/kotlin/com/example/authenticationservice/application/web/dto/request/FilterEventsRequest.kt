package com.example.authenticationservice.application.web.dto.request

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate
import javax.validation.constraints.*

data class FilterEventsRequest(
    @JsonProperty("date") @field:DateTimeFormat(pattern = "yyyy/MM/dd") @field:Future(message = "A data do evento deve estar no futuro") val date: LocalDate?,
    @JsonProperty("cep") val cep: String?,
    @JsonProperty("payment") @field:Min(value = 10, message = "O valor mínimo de pagamento é 10") val payment: Double?,
    @JsonProperty("instrumentsId") val instrumentsId: List<Long>?
)
