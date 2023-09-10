package com.example.authenticationservice.domain.repositories

import com.example.authenticationservice.application.web.dto.response.DeleteNotificationDto
import com.example.authenticationservice.application.web.dto.response.JobRequestDto
import com.example.authenticationservice.application.web.dto.response.NotificationTypeDto
import com.example.authenticationservice.domain.entities.Notification
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface NotificationRepository: JpaRepository<Notification, Long> {
    @Query("""
        select new com.example.authenticationservice.application.web.dto.response.JobRequestDto(notification.id, notification.notificationType, notification.jobRequest)
            from Notification notification
                    where notification.user.id = :userId
                        order by notification.id desc
    """)
    fun findJobRequestDtoByUserId(userId: Long): List<JobRequestDto>

    @Query("""
        select new com.example.authenticationservice.application.web.dto.response.DeleteNotificationDto(notification.notificationType, notification.jobRequest.id, notification.user.id)
            from Notification notification
                    where notification.user.id = :userId
                        and notification.id = :notificationId
                            and notification.notificationType = :notificationType
    """)
    fun findDeleteNotificationDtoByUserIdAndNotificationId(userId: Long, notificationId: Long, notificationType: NotificationTypeDto): com.example.authenticationservice.application.web.dto.response.DeleteNotificationDto?

    @Modifying
    @Query("delete from Notification notification where notification.jobRequest.id = :fkEventJob ")
    fun deleteByJobRequestId(fkEventJob: Long)

    @Modifying
     fun deleteByJobRequestEventJobEventId(eventId: Long)
}
//    @Query("delete from Notification notification where notification.jobRequest.eventJob.event.id = :eventId")
