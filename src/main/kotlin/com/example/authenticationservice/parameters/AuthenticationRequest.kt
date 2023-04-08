package com.example.authenticationservice.parameters

import com.example.authenticationservice.dto.TypeUserDto
import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

class AuthenticationRequest(
        @JsonProperty("email") @field:Email @field:NotBlank
        val email: String,
        @JsonProperty("password") @field:NotBlank @field:Size(min = 8, max = 15)
        var password: String,
        @JsonProperty("type") @field:NotBlank @field:Size(min = 8, max = 15)
        val type : TypeUserDto
) {

}