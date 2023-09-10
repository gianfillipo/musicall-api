package com.example.authenticationservice.application.web.dto.request

import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.NotNull

data class DeleteEventJobRequest (
    @JsonProperty("id") @field:NotNull val id: Long?,
    @JsonProperty("fkEvent") @field:NotNull val fkEvent: Long?
) {
}

