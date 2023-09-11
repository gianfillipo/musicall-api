package com.example.authenticationservice.application.web.dto.request

import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.*

data class RegisterMusicianRequest (
    @JsonProperty("description") @field:NotNull @field:NotBlank val description: String?,
    @JsonProperty("cep") @field:NotNull @field:NotBlank val cep: String?,
    @JsonProperty("imageUrl") val imageUrl: String?
)