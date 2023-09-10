package com.example.authenticationservice.application.web.dto.request

import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

data class SetPasswordRequest(
        @JsonProperty("password") @field:NotNull @field:NotBlank @field:Size(min = 8, max = 30) var password: String,
        @JsonProperty("token") @field:NotNull @field:NotBlank var token: String
)
