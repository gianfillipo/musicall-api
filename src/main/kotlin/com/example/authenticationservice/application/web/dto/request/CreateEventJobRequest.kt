package com.example.authenticationservice.application.web.dto.request

import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.NotNull

data class CreateEventJobRequest(
        @JsonProperty("fkEvent") @field:NotNull(message = "O ID do evento não pode ser nulo") val fkEvent: Long?,
        @JsonProperty("jobs") @field:NotNull(message = "A lista de trabalhos não pode ser nula") val jobs: List<com.example.authenticationservice.application.web.dto.request.JobRequest>?
)
