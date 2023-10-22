package com.example.authenticationservice.domain.repositories

import com.example.authenticationservice.application.web.dto.response.EventDto
import com.example.authenticationservice.application.web.dto.response.MusicianInfoResponse
import com.example.authenticationservice.domain.entities.Musician
import com.example.authenticationservice.domain.entities.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query


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
}
