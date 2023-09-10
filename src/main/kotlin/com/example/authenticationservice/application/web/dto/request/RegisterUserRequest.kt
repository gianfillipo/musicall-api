package com.example.authenticationservice.application.web.dto.request

import com.example.authenticationservice.application.web.dto.response.TypeUserDto
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate
import javax.validation.constraints.*

data class RegisterUserRequest(
        @JsonProperty("type")
        @field:NotNull(message = "O tipo não pode ser nulo.")
        val type: TypeUserDto?,

        @JsonProperty("name")
        @field:NotNull(message = "O nome não pode ser nulo.")
        @field:NotBlank(message = "O nome não pode estar em branco.")
        val name: String?,

        @JsonProperty("cpf")
        @field:NotNull(message = "O CPF não pode ser nulo.")
        @field:NotBlank(message = "O CPF não pode estar em branco.")
        val cpf: String?,

        @JsonProperty("birthDate")
        @field:NotNull(message = "A data de nascimento não pode ser nula.")
        @field:DateTimeFormat(pattern = "yyyy/MM/dd")
        @field:Past(message = "A data de nascimento deve ser anterior à data atual.")
        val birthDate: LocalDate?,

        @JsonProperty("telephone")
        @field:NotNull(message = "O telefone não pode ser nulo.")
        @field:NotBlank(message = "O telefone não pode estar em branco.")

        val telephone: String?,

        @JsonProperty("email")
        @field:NotNull(message = "O email não pode ser nulo.")
        @field:Email(message = "O email deve ser um endereço de email válido.")
        @field:NotBlank(message = "O email não pode estar em branco.")
        val email: String?,

        @JsonProperty("password")
        @field:NotNull(message = "A senha não pode ser nula.")
        @field:NotBlank(message = "A senha não pode estar em branco.")
        @field:Size(min = 8, max = 15, message = "A senha deve ter entre 8 e 15 caracteres.")
        var password: String?
)
