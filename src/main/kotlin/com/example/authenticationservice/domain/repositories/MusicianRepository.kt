package com.example.authenticationservice.domain.repositories

import com.example.authenticationservice.application.web.dto.response.*
import com.example.authenticationservice.domain.entities.Musician
import com.example.authenticationservice.domain.entities.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import javax.persistence.QueryHint


interface MusicianRepository : MusicianRepositoryCustom, JpaRepository<Musician, Long> {
    fun existsByUser(user: User) : Boolean
    fun getByUser(user: User): Musician?

    @Query("SELECT m.cep FROM Musician m WHERE m.user.id = :userId")
    fun findCepByUserId(userId: Long): String?
    @Query("SELECT m FROM Musician m WHERE m.user.id = :id")
    fun getMusicianByUserId(id: Long): Musician?
    @Query("SELECT CASE WHEN COUNT(m) > 0 THEN true ELSE false END\n FROM Musician m WHERE m.user.id = :userId")
    fun existsByUserId(userId: Long): Boolean

    @Query("SELECT mi.instrument.id FROM Musician m JOIN m.musicianInstruments mi WHERE m.user.id = :userId")
    fun findInstrumentIdsByUserId(userId: Long): List<Long>
    fun findByUserId(id: Long): Musician?

    @Query("SELECT m.id FROM Musician m WHERE m.user.id = :fkUser")
    fun findIdByUserId(fkUser: Long): Long?

    @Query("SELECT COUNT(m.cep) FROM Musician m WHERE m.user.id = :userId")
    fun validationMusician(userId: Long): String


    @Query("""
        SELECT new com.example.authenticationservice.application.web.dto.response.MusicianInfoResponse(
            m.id,
            u.name,
            m.description,
            m.cep,
            m.imageUrl
        )
        FROM Musician m 
        JOIN m.user u
        WHERE m.id = :musicianId
    """)
//    m.musicianInstruments
    fun findMusicianInfoById(musicianId: Long): MusicianInfoResponse?

    @Query("""
    SELECT
        NEW com.example.authenticationservice.application.web.dto.response.InvitesKpiMusicianResponse(
            jr.musician.id,
            COUNT(*)
        )
    FROM
        JobRequest jr
    WHERE
        jr.musician.id = :musicianId
    GROUP BY
        jr.musician.id
""")
    fun getJobRequestByMusicianId(musicianId: Long): InvitesKpiMusicianResponse

    @Query("""
    SELECT
        NEW com.example.authenticationservice.application.web.dto.response.TotalMatchsKpiMusician(
            jr.musician.id,
            COUNT(*)
        )
    FROM
        JobRequest jr
    WHERE
        jr.musician.id = :musicianId
        AND jr.organizerConfirmed = 1
        AND jr.musicianConfirmed = 1
    GROUP BY
        jr.musician.id
    """)
    fun getAllMatchesByMusiciaId(musicianId: Long): TotalMatchsKpiMusician

    @Query("""
    SELECT
        NEW com.example.authenticationservice.application.web.dto.response.EventJobPerInstrumentResponse(
            i.name,
            COUNT(*)
        )
    FROM
        EventJob ej
    JOIN
        ej.instrument i
    GROUP BY
        i.name
    ORDER BY
        COUNT(*) DESC
    """)
    fun findTop5InstrumentsByVacancies(): List<EventJobPerInstrumentResponse>

    @Query("""
    SELECT
        NEW com.example.authenticationservice.application.web.dto.response.InvitePerInstrumentResponse(
            i.name,
            i.id,
            COUNT(i.id) AS inviteCount
        )
    FROM
        Notification n
    JOIN
        n.jobRequest jr
    JOIN
        jr.eventJob ej
    JOIN
        ej.instrument i
    WHERE
        n.notificationType = 1
        AND jr.musician.id = :musicianId
    GROUP BY
        i.id, i.name
    """)
    fun getAllInvitesByInstruments(musicianId: Long): List<InvitePerInstrumentResponse>
}
