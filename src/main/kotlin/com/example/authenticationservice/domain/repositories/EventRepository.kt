package com.example.authenticationservice.domain.repositories

import com.example.authenticationservice.application.web.dto.response.EventsInfoForMusicianResponse
import com.example.authenticationservice.application.web.dto.response.CalendarEventByIdDto
import com.example.authenticationservice.application.web.dto.response.EventDto
import com.example.authenticationservice.application.web.dto.response.EventsInfoResponse
import com.example.authenticationservice.domain.entities.Event
import com.example.authenticationservice.domain.entities.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.time.LocalDate

interface EventRepository : EventRepositoryCustom, JpaRepository<Event, Long> {
    abstract fun findByIdAndUserAndFinalized(id: Long, user: User, finalized: Boolean): Event?
  //    @Query("SELECT ej FROM EventJob ej JOIN ej.instrument i WHERE i.id IN :instrumentIds AND ej.event.finalized = false AND ej.event.eventDate >= :currentDate")
    fun existsByEventDateAndFinalized(eventDate: LocalDate, b: Boolean): Boolean
    @Query("SELECT count(e) > 0 FROM Event e WHERE e.id = :id AND e.user.id = :userId AND e.finalized = false")
    fun existsByIdAndUserIdAndFinalizedFalse(id: Long, userId: Long): Boolean

    @Query("SELECT e.id FROM Event e WHERE e.id = :id AND e.user.id = :userId AND e.finalized = false")
    fun findIdByIdAndUserIdAndFinalizedFalse(id: Long, userId: Long): Long?

    @Query("""
        select new com.example.authenticationservice.application.web.dto.response.CalendarEventDto(e)
            from Event e
                  where e.user.id = :userId
                  order by e.eventDate asc
    """)
    fun findCalendarEventsByOganizer(userId: Long): List<com.example.authenticationservice.application.web.dto.response.CalendarEventDto>

    @Query("""
        select new com.example.authenticationservice.application.web.dto.response.CalendarEventByIdDto(e)
            from Event e
                  where e.user.id = :userId
                  and e.id = :eventId 
                  order by e.eventDate asc
                  
    """)
    fun findCalendarEventsByOganizerByEventId(userId: Long, eventId:Long): List<CalendarEventByIdDto>

    @Query("SELECT e FROM Event e WHERE e.id = :id")
    fun findEventById(id: Long): EventDto?

    @Query("""
        select new com.example.authenticationservice.application.web.dto.response.EventsInfoForMusicianResponse(
            e.id,
            e.name,
            e.eventDate,
            e.startHour,
            ej.instrument.name
        )
            from Event e
            join e.eventJob ej
            where ej.musician.id = :musicianId
            order by e.eventDate asc
    """)
    fun findEventInfoByMusicianId(musicianId: Long): List<EventsInfoForMusicianResponse>

    @Query("""
        select new com.example.authenticationservice.application.web.dto.response.EventsInfoResponse(
            e.name,
            e.aboutEvent,
            e.cep,
            e.number,
            e.complement,
            e.eventDate,
            e.startHour,
            e.durationHours,
            e.imageUrl
        )
            from Event e
            join e.eventJob ej
            where ej.id = :eventJobId
    """)
    fun getEventInfoById(eventJobId: Long): EventsInfoResponse?
}
