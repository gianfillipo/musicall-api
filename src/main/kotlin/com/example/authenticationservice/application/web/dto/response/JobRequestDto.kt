package com.example.authenticationservice.application.web.dto.response

import com.example.authenticationservice.domain.entities.JobRequest

class
JobRequestDto (
   val id: Long,
   val jobRequestId: Long,
   val typeDto: NotificationTypeDto,
   val musicianConfirmed: Boolean,
   val organizerConfirmed: Boolean,
   val eventJobDto: JobRequestEventJobDto,
   val eventDto: JobRequestEventDto,
   val musicianFk: Long
) {
   constructor(notificationId: Long, notificationTypeDto: NotificationTypeDto, jobRequest: JobRequest): this(
      id = notificationId,
      jobRequestId = jobRequest.id,
      typeDto = notificationTypeDto,
      musicianConfirmed = jobRequest.musicianConfirmed,
      organizerConfirmed = jobRequest.organizerConfirmed,
      eventJobDto = JobRequestEventJobDto(jobRequest.eventJob.id, jobRequest.eventJob.event.id, jobRequest.eventJob.instrument.name),
      eventDto = JobRequestEventDto(jobRequest.eventJob.event.name, jobRequest.eventJob.event.eventDate),
      musicianFk = jobRequest.musician!!.id
   )

   constructor() : this(
        id = 0,
        jobRequestId = 0,
        typeDto = NotificationTypeDto.REQUEST,
        musicianConfirmed = false,
        organizerConfirmed = false,
        eventJobDto = JobRequestEventJobDto(),
        eventDto = JobRequestEventDto(),
        musicianFk = 0
   )
}
