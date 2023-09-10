package com.example.authenticationservice.application.web.dto.request

import com.example.authenticationservice.response.TypeUserDto
import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

data class AuthenticationRequest(
        @JsonProperty("email")  @field:Email @field:NotBlank @field:NotNull
        val email: String?,
        @JsonProperty("password") @field:NotNull @field:NotBlank @field:Size(min = 8, max = 30)
        var password: String?,
        @JsonProperty("type") @field:NotNull
        val type : TypeUserDto?
)
