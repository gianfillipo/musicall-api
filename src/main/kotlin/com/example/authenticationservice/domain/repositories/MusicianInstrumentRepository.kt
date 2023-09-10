package com.example.authenticationservice.domain.repositories

import com.example.authenticationservice.domain.entities.Instrument
import com.example.authenticationservice.domain.entities.Musician
import com.example.authenticationservice.domain.entities.MusicianInstrument
import org.springframework.data.jpa.repository.JpaRepository


interface MusicianInstrumentRepository : JpaRepository<MusicianInstrument, Long> {
    abstract fun existsByInstrumentIn(instrumentsOfUser: List<Instrument>): Boolean
    abstract fun findByMusician(musician: Musician): List<MusicianInstrument>
}
