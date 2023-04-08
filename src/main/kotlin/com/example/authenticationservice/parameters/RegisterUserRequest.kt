package com.example.authenticationservice.parameters

import com.example.authenticationservice.dto.TypeUserDto
import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

data class RegisterUserRequest(
        @JsonProperty("type") val type: TypeUserDto,
        @JsonProperty("name") @field:NotBlank val name: String,
        @JsonProperty("lastName") @field:NotBlank val lastName: String,
        @JsonProperty("cpf") @field:NotBlank val cpf: String,
        @JsonProperty("birthDate") @field:NotBlank val birthDate: String,
        @JsonProperty("telephone") @field:NotBlank @field:Pattern(
        regexp = "(\\(?\\d{2}\\)?\\s)?(\\d{4,5}\\-\\d{4})",
        message = "Envie um telefone válido"
    ) val telephone: String,
        @JsonProperty("cep") @field:NotBlank @field:Pattern(
        regexp = "^\\d{5}-?\\d{3}$",
        message = "Envie um cep válido"
    )val cep: String,
        @JsonProperty("address") @field:NotBlank val address: String,
        @JsonProperty("email") @field:Email @field:NotBlank val email: String,
        @JsonProperty("password") @field:NotBlank @field:Size(min = 8, max = 15) var password: String

)
