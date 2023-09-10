package com.example.authenticationservice.application.web.dto.response

data class JobRequestEventJobDto (
    val id: Long,
    val fkEvento : Long,
    val instrumentName : String
) {
    constructor(): this(
        id = 0,
        fkEvento = 0,
        instrumentName = ""
    )
}