package com.example.authenticationservice.domain.repositories

import com.example.authenticationservice.application.web.dto.response.InstrumentIdAndEventCepDto
import com.example.authenticationservice.domain.entities.Event
import com.example.authenticationservice.domain.entities.EventJob
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDate
import java.util.*

interface EventJobRepository : EventRepositoryCustom, JpaRepository<EventJob, Long> {
    @Query("SELECT count(e) > 0 FROM EventJob e WHERE e.id = :eventJobId AND e.event.user.id = :userId AND e.event.finalized = false")
    fun existsByIdAndEventUserIdAndEventFinalizedFalse(@Param("eventJobId") id: Long, @Param("userId") userId: Long): Boolean
    abstract fun existsByIdAndEvent(id: Long, event: Event): Boolean
    @Query("SELECT ej.event FROM EventJob ej WHERE ej.id = :fkEventJob")
    fun findEventById(@Param("fkEventJob") fkEventJob: Long): Event?
    fun getById(fkEventJob: Long): EventJob?
    @Query("SELECT COUNT(e) > 0 FROM EventJob e WHERE e.event.eventDate = :eventDate AND e.musician.id = :musicianId")
    fun existsByEventDateAndMusicianId(eventDate: LocalDate, musicianId: Long): Boolean
    @Modifying
    fun deleteByEventId(eventId: Long)
    @Query("SELECT new com.example.authenticationservice.application.web.dto.response.InstrumentIdAndEventCepDto(instrument.id, e.event.cep) FROM EventJob e WHERE e.id = :eventJobId AND e.event.user.id = :userId")
    fun findInstrumentIdAndEventCepByIdAndUserId(eventJobId: Long, userId: Long): InstrumentIdAndEventCepDto?
}
