package com.example.authenticationservice.domain.repositories

import com.example.authenticationservice.application.web.dto.response.InstrumentDto
import com.example.authenticationservice.application.web.dto.response.InstrumentsPerMusician
import com.example.authenticationservice.domain.entities.Instrument
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface InstrumentRepository : JpaRepository<Instrument, Long> {
    @Query("SELECT new com.example.authenticationservice.application.web.dto.response.InstrumentDto(i.id, i.name) FROM Instrument i WHERE i.id IN :instrumentIds")
    fun findInstrumentDtoByIdIn(instrumentIds: List<Long>): List<InstrumentDto>

    @Query("""
        SELECT
    NEW com.example.authenticationservice.application.web.dto.response.InstrumentsPerMusician(
        i.name AS instrumentName,
        COUNT(mi.instrument.id) AS count
    )
    FROM
        MusicianInstrument mi
    JOIN
        Instrument i ON mi.instrument.id = i.id
    GROUP BY
        mi.instrument.id, i.name
        """)
    fun getInstrumentsPerMusicians(): List<InstrumentsPerMusician>
}