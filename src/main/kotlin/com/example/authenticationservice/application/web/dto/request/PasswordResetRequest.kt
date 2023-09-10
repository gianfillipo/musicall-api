package com.example.authenticationservice.application.web.dto.request

import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class PasswordResetRequest(@JsonProperty("email") @field:NotNull @field:NotBlank val email: String)
