package com.example.authenticationservice.application.web.dto.request

import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.NotNull

data class DeleteEventRequest (
    @JsonProperty("id") @field:NotNull(message = "Id não pode ser nulo")val id: Long?
)
