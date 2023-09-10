package com.example.authenticationservice.application.web.dto.response


data class MusicianEventJobDto(
    val id: Long,
    val name: String,
    val cep: String,
    val imageUrl: String,
    val instrumentName: String,
    var distance: Int = 0
) {
    constructor(id: Long, name: String, cep: String, imageUrl: String, instrumentName: String): this(
            id = id,
            name = name,
            cep = cep,
            imageUrl = imageUrl,
            instrumentName = instrumentName,
            distance = 0
    )
}
