package com.example.authenticationservice.application.web.dto.response

data class InstrumentDto (
    val id: Long,
    val name: String
) {
    constructor(): this(
        id = 0,
        name = ""
    )
}