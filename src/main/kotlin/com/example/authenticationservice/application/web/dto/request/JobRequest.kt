package com.example.authenticationservice.application.web.dto.request

import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.Min

class JobRequest (
    @JsonProperty("instrumentId") val instrumentId: Long,
    @JsonProperty("quantity") @field:Min(1) val quantity: Int,
    @JsonProperty("payment") @field:Min(10) val payment: Double
) {
}