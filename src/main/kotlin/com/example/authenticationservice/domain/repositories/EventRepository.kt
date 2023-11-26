package com.example.authenticationservice.domain.repositories

import com.example.authenticationservice.application.web.dto.response.*
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
                  and e.finalized = false
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

    @Query("""
        SELECT COUNT(e) FROM Event e 
        WHERE FUNCTION('MONTH', e.eventDate) = FUNCTION('MONTH', CURRENT_DATE) 
        AND FUNCTION('YEAR', e.eventDate) = FUNCTION('YEAR', CURRENT_DATE) 
        AND e.user.id = :user
    """)
    fun getEventsPerCurrentMonth(user: Long): Long

    @Query("""
    SELECT
        new com.example.authenticationservice.application.web.dto.response.EventCountProjection(
        SUM(CASE WHEN MONTH(e.eventDate) = MONTH(CURRENT_DATE) AND YEAR(e.eventDate) = YEAR(CURRENT_DATE) THEN 1 ELSE 0 END) AS currentMonthCount,
        SUM(CASE WHEN MONTH(e.eventDate) = MONTH(CURRENT_DATE - 1) AND YEAR(e.eventDate) = YEAR(CURRENT_DATE - 1) THEN 1 ELSE 0 END) AS lastMonthCount
    )
    FROM
        Event e
    WHERE
        ((MONTH(e.eventDate) = MONTH(CURRENT_DATE) AND YEAR(e.eventDate) = YEAR(CURRENT_DATE))
        OR
        (MONTH(e.eventDate) = MONTH(CURRENT_DATE - 1) AND YEAR(e.eventDate) = YEAR(CURRENT_DATE)))
        AND e.user.id = :user
    """)
    fun getEventCounts(user: Long): EventCountProjection


    @Query("""
SELECT COUNT(e)
FROM com.example.authenticationservice.domain.entities.Event e
WHERE FUNCTION('MONTH', e.eventDate) = FUNCTION('MONTH', CURRENT_DATE)
    AND FUNCTION('YEAR', e.eventDate) = FUNCTION('YEAR', CURRENT_DATE)
    AND e.user.id = :user
""")
    fun getCurrentMonthEventCount(user: Long): Long

    @Query("""
    SELECT COUNT(e)
    FROM com.example.authenticationservice.domain.entities.Event e
    WHERE FUNCTION('MONTH', e.eventDate) = FUNCTION('MONTH', CURRENT_DATE - 1)
    AND FUNCTION('YEAR', e.eventDate) = FUNCTION('YEAR', CURRENT_DATE - 1)
    AND e.user.id = :user
""")
    fun getLastMonthEventCount(user: Long): Long

    @Query("""
    SELECT NEW com.example.authenticationservice.application.web.dto.response.HoursPerMonth(
        FUNCTION('MONTH', e.eventDate),
        SUM(e.durationHours)
    )
    FROM com.example.authenticationservice.domain.entities.Event e
    WHERE e.finalized = true AND e.user.id = :user
    GROUP BY FUNCTION('MONTH', e.eventDate)
    """)
    fun getEventHoursPerMouth(user: Long): List<HoursPerMonth>


}
