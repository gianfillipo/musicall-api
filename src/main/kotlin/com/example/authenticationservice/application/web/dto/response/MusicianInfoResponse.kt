package com.example.authenticationservice.application.web.dto.response

import com.example.authenticationservice.domain.entities.Instrument
import com.example.authenticationservice.domain.entities.MusicianInstrument

class MusicianInfoResponse (
    val musicianId: Long,
    val name: String,
    var description: String,
    var cep: String,
    var imageUrl: String
) {

    var instruments = emptyList<String>()
}