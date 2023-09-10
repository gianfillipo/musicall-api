package com.example.authenticationservice.application.web.dto.request

import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.NotNull

class CreateJobRequestRequest (
    @JsonProperty("fkEventJob") @NotNull val fkEventJob: Long?
)
