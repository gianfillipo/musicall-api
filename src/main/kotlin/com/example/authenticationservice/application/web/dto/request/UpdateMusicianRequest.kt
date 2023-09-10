package com.example.authenticationservice.application.web.dto.request

import com.fasterxml.jackson.annotation.JsonProperty

class UpdateMusicianRequest (
    @JsonProperty("description") val description: String?,
    @JsonProperty("cep") val cep: String?,
    @JsonProperty("imageUrl") val imageUrl: String?
)
