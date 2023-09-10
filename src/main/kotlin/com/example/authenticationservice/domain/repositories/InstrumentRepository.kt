package com.example.authenticationservice.domain.repositories

import com.example.authenticationservice.response.InstrumentDto
import com.example.authenticationservice.domain.entities.Instrument
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface InstrumentRepository : JpaRepository<Instrument, Long> {
    @Query("SELECT new com.example.authenticationservice.dto.InstrumentDto(i.id, i.name) FROM Instrument i WHERE i.id IN :instrumentIds")
    fun findInstrumentDtoByIdIn(instrumentIds: List<Long>): List<InstrumentDto>
}