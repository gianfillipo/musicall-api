package com.example.authenticationservice.dao

import com.example.authenticationservice.model.Instrument
import com.example.authenticationservice.model.MusicianInstrument
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query


interface MusicianInstrumentRepository : JpaRepository<MusicianInstrument, Long> {
    fun findByMusicianId(fkMusician: Long): List<MusicianInstrument>
    abstract fun existsByInstrumentIn(instrumentsOfUser: List<Instrument>): Boolean

}
