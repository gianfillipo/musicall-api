package com.example.authenticationservice.application.web.dto.mapper

import com.example.authenticationservice.application.web.dto.response.MusicianDto
import com.example.authenticationservice.domain.entities.Musician
import org.springframework.stereotype.Component

@Component
class MusicianMapper {
    fun toDto(musician: Musician) : MusicianDto {
        return MusicianDto(
            musician.description,
            musician.cep
        )
    }

}