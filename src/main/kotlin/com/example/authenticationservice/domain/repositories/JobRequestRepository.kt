package com.example.authenticationservice.domain.repositories

import com.example.authenticationservice.domain.entities.JobRequest
import com.example.authenticationservice.domain.entities.Musician
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface JobRequestRepository: JpaRepository<JobRequest, Long> {
    @Query("SELECT COUNT(j) > 0 FROM JobRequest j WHERE j.musician.id = :musicianId AND j.eventJob.event.id = :eventId")
    fun existsByMusicianIdAndEventId(musicianId: Long, eventId: Long): Boolean

    @Query("""
        SELECT new com.example.authenticationservice.application.web.dto.response.DeleteJobRequestDto(j.id, j.organizerConfirmed)
            FROM JobRequest j 
                WHERE j.eventJob.id = :fkEventJob 
                    AND j.musician.id = :musicianId
     """
    )
    fun findIdAndOrganizerConfirmedByEventJobIdAndMusicianId(fkEventJob: Long, musicianId: Long): com.example.authenticationservice.application.web.dto.response.DeleteJobRequestDto?

    @Query("SELECT j.musician.user.id FROM JobRequest j WHERE j.id = :id AND j.eventJob.event.user.id = :userId AND j.musicianConfirmed = true AND j.organizerConfirmed = false")
    fun findUserIdByIdAndUserIdAndMusicianConfirmedTrue(id:Long, userId: Long): Long?


    @Query("SELECT j.eventJob.event.user.id FROM JobRequest j WHERE j.id = :id AND j.musician.id = :userId AND j.musicianConfirmed = false AND j.organizerConfirmed = true")
    fun findOrganizerdByIdAndUserIdAndMusicianConfirmedTrue(id:Long, userId: Long): Long?

    @Query("SELECT j.eventJob.event.user.id FROM JobRequest j WHERE j.id = :jobRequestId AND j.musicianConfirmed = false AND j.organizerConfirmed = true")
    fun findOrganizerForNotification(jobRequestId: Long): Long?

    @Query("SELECT j.eventJob.musician.id FROM JobRequest j WHERE j.id = :id AND j.musician.id = :userId AND j.musicianConfirmed = false AND j.organizerConfirmed = true")
    fun findMusicianByIdAndUserIdAndOrganizerConfirmedTrue(id:Long, userId: Long): Long?

    @Modifying
    @Query("UPDATE JobRequest j SET j.organizerConfirmed = true WHERE j.id = :id")
    fun updateOrganizerConfirmedTrueById(id: Long)

    @Modifying
    @Query("UPDATE JobRequest j SET j.musicianConfirmed = true, j.musician.id = :musicianId WHERE j.id = :id")
    fun updateMusicianConfirmedTrueById(id: Long, musicianId: Long)

    @Modifying
    fun deleteByEventJobId(eventJobId: Long)
    @Modifying
    fun deleteByEventJobEventId(eventId: Long)

    @Query("SELECT j.eventJob.id FROM JobRequest j WHERE j.id = :id")
    fun findEvenJobIdById(id: Long): Long?
}
