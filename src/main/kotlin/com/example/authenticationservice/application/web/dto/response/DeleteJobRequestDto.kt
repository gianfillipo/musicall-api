package com.example.authenticationservice.application.web.dto.response

class DeleteJobRequestDto (
    val id: Long,
    val organizerConfirmed: Boolean
) {
    constructor(): this(
        id = 0,
        organizerConfirmed = false
    )
}