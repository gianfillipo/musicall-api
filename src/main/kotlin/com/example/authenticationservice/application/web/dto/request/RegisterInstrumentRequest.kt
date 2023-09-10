package com.example.authenticationservice.application.web.dto.request

import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.NotNull

data class RegisterInstrumentRequest (
    @JsonProperty("fkInstrument") @field:NotNull val fkInstrument: List<Long>?
)