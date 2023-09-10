package com.example.authenticationservice.domain.service

import com.example.authenticationservice.domain.entities.*
import com.example.authenticationservice.domain.entities.JobRequest
import com.example.authenticationservice.domain.repositories.*
import com.example.authenticationservice.application.web.dto.response.*
import com.example.authenticationservice.application.web.dto.request.*
import com.example.authenticationservice.application.config.security.JwtTokenProvider
import com.example.authenticationservice.application.web.utils.GoogleMapsUtils
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import javax.servlet.http.HttpServletRequest

@Service
class OrganizerService (
    @Autowired private val eventRepository: EventRepository,
    @Autowired private val jwtTokenProvider: JwtTokenProvider,
    @Autowired private val userRepository: UserRepository,
    @Autowired private val instrumentRepository: InstrumentRepository,
    @Autowired private val jobRequestRepository: JobRequestRepository,
    @Autowired private val notificationRepository: NotificationRepository,
    @Autowired private val eventJobRepository : EventJobRepository,
    @Autowired private val musicianService: MusicianService,
    @Autowired private val googleMapsService: GoogleMapsUtils
) {
    fun createEvent(createEventRequest: com.example.authenticationservice.application.web.dto.request.CreateEventRequest, req : HttpServletRequest) : com.example.authenticationservice.application.web.dto.response.CreateEventDto {
        val token  = jwtTokenProvider.resolveToken(req) ?: throw ResponseStatusException(HttpStatus.FORBIDDEN, "Tipo de usuário inválido")
        val id = jwtTokenProvider.getId(token).toLong()
        val user = userRepository.getById(id) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado")
        if (eventRepository.existsByEventDateAndFinalized(createEventRequest.eventDate!!, false)) throw ResponseStatusException(HttpStatus.CONFLICT, "Você já tem um evento nessa data")

        val event = Event(createEventRequest, user)

        eventRepository.save(event)

        return com.example.authenticationservice.application.web.dto.response.CreateEventDto(event)
    }

    fun createEventJob(createEventJobRequest: com.example.authenticationservice.application.web.dto.request.CreateEventJobRequest, req: HttpServletRequest): List<EventJobDto> {
        val token  = jwtTokenProvider.resolveToken(req) ?: throw ResponseStatusException(HttpStatus.FORBIDDEN, "Tipo de usuário inválido")
        val id = jwtTokenProvider.getId(token).toLong()
        val eventId = eventRepository.findIdByIdAndUserIdAndFinalizedFalse(createEventJobRequest.fkEvent!!, id) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Você não pode criar jobs para esse evento")
        val instrumentsEventJobIds = createEventJobRequest.jobs!!.map { it.instrumentId }

        val instruments = instrumentRepository.findInstrumentDtoByIdIn(instrumentsEventJobIds)
        val instrumentsHash = hashMapOf<Long, InstrumentDto?>()
        instruments.forEach { instrumentsHash[it.id] = it }


        val eventJobs = mutableListOf<EventJob>()
        createEventJobRequest.jobs!!.forEach {
            if (instrumentsHash.containsKey(it.instrumentId)) for(i in 1 .. it.quantity) { eventJobs.add(EventJob(Event(eventId), Instrument(id = it.instrumentId, name = instrumentsHash[it.instrumentId]!!.name), it.payment)) }
            else throw ResponseStatusException(HttpStatus.NOT_FOUND, "Instrumento não encontrado")
        }

       eventJobRepository.saveAll(eventJobs)
        return eventJobs.map{ EventJobDto(it) }
    }

    @Transactional
    fun deleteEvent(req: HttpServletRequest, deleteEventRequest: com.example.authenticationservice.application.web.dto.request.DeleteEventRequest) {
        val token  = jwtTokenProvider.resolveToken(req) ?: throw ResponseStatusException(HttpStatus.FORBIDDEN, "Tipo de usuário invalido")
        val id = jwtTokenProvider.getId(token).toLong()

        if (!eventRepository.existsByIdAndUserIdAndFinalizedFalse(deleteEventRequest.id!!, id)) throw ResponseStatusException(HttpStatus.NOT_FOUND, "Evento não encontrado")

        notificationRepository.deleteByJobRequestEventJobEventId(deleteEventRequest.id!!)
        jobRequestRepository.deleteByEventJobEventId(deleteEventRequest.id!!)
        eventJobRepository.deleteByEventId(deleteEventRequest.id!!)
        eventRepository.deleteById(deleteEventRequest.id!!)
}

    fun updateEvent(updateEventRequest: com.example.authenticationservice.application.web.dto.request.UpdateEventRequest, req: HttpServletRequest): com.example.authenticationservice.application.web.dto.response.CreateEventDto {
        val token  = jwtTokenProvider.resolveToken(req) ?: throw ResponseStatusException(HttpStatus.FORBIDDEN, "ResponseStatusException(HttpStatus.NOT_FOUND, \"Usuário não encontrado\")")
        val id = jwtTokenProvider.getId(token).toLong()
        val user = userRepository.getById(id) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado")
        val event = eventRepository.findByIdAndUserAndFinalized(updateEventRequest.id!!, user, false) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Evento não encontrado")

        var hasChanges = false

        if (updateEventRequest.name != null) {
            event.name = if (updateEventRequest.name == event.name) throw ResponseStatusException(HttpStatus.CONFLICT, "O nome do evento é o mesmo") else updateEventRequest.name
            hasChanges = true
        }

        if (updateEventRequest.imageUrl != null) {
            event.imageUrl = if (updateEventRequest.imageUrl == event.imageUrl) throw ResponseStatusException(HttpStatus.CONFLICT, "A url da imagem é a mesma") else updateEventRequest.imageUrl
            hasChanges = true
        }

        if (updateEventRequest.aboutEvent != null) {
            event.aboutEvent = if (updateEventRequest.aboutEvent == event.aboutEvent) throw ResponseStatusException(HttpStatus.CONFLICT, "A descrição do evento é a mesma") else updateEventRequest.aboutEvent
            hasChanges = true
        }

        if (updateEventRequest.cep != null) {
            event.cep = if (updateEventRequest.cep == event.cep) throw ResponseStatusException(HttpStatus.CONFLICT, "O cep é o mesmo") else updateEventRequest.cep
            hasChanges = true
        }

        if (updateEventRequest.number != null) {
            event.number = if (updateEventRequest.number == event.number) throw ResponseStatusException(HttpStatus.CONFLICT, "O número do endereço é o mesmo") else updateEventRequest.number
            hasChanges = true
        }

        if (updateEventRequest.complement != null) {
            event.complement = if (updateEventRequest.complement == event.complement) throw ResponseStatusException(HttpStatus.CONFLICT, "O local é o mesmo") else updateEventRequest.complement
            hasChanges = true
        }

        if (updateEventRequest.eventDate != null){
            event.eventDate = if (updateEventRequest.eventDate == event.eventDate) throw ResponseStatusException(HttpStatus.CONFLICT, "A data do evento é a mesma") else updateEventRequest.eventDate
            hasChanges = true
        }

        if (updateEventRequest.startHour != null){
            event.startHour = if (updateEventRequest.startHour == event.startHour) throw ResponseStatusException(HttpStatus.CONFLICT, "A data de início é a mesma") else updateEventRequest.startHour
            hasChanges = true
        }

        if (updateEventRequest.durationHours != null) {
            event.durationHours = if (updateEventRequest.durationHours == event.durationHours) throw ResponseStatusException(HttpStatus.CONFLICT, "A duração em horas é a mesma") else updateEventRequest.durationHours
            hasChanges = true
        }

        if (!hasChanges) throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Você não fez nenhuma alteração")

        eventRepository.save(event)

        return com.example.authenticationservice.application.web.dto.response.CreateEventDto(event)
    }

    @Transactional
    fun deleteEventJob(req: HttpServletRequest, eventJobId: Long?) {
        val token  = jwtTokenProvider.resolveToken(req) ?: throw ResponseStatusException(HttpStatus.FORBIDDEN, "Tipo de usuário inválido")
        val id = jwtTokenProvider.getId(token).toLong()

        if (!eventJobRepository.existsByIdAndEventUserIdAndEventFinalizedFalse(eventJobId!!, id)) throw ResponseStatusException(HttpStatus.NOT_FOUND, "Evento não encontrado")

        notificationRepository.deleteByJobRequestId(eventJobId!!)
        jobRequestRepository.deleteByEventJobId(eventJobId)
        eventJobRepository.deleteById(eventJobId)
    }

    @Transactional
    fun approveJobRequest(id: Long, jobRequestId: Long?) {
        val userId = jobRequestRepository.findUserIdByIdAndUserIdAndMusicianConfirmedTrue(jobRequestId!!, id) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Vocẽ não pode aprovar esse job")
        jobRequestRepository.updateOrganizerConfirmedTrueById(jobRequestId)

        val user = User()
        user.id = userId

        val jobRequest = JobRequest()
        jobRequest.id = jobRequestId!!

        notificationRepository.save(
            Notification(
                user = user,
                jobRequest = jobRequest,
                notificationType = NotificationTypeDto.CONFIRM
            )
        )
    }
    fun findMusicianByEventLocation(req: HttpServletRequest, eventJobId: Long, filterMusicianRequest: com.example.authenticationservice.application.web.dto.request.FilterMusicianRequest, pageable: Pageable): PageImpl<MusicianEventJobDto> {
        val token  = jwtTokenProvider.resolveToken(req) ?: throw ResponseStatusException(HttpStatus.FORBIDDEN, "Tipo de usuário inválido")
        val userId = jwtTokenProvider.getId(token).toLong()
        val instrumentIdAndEventCepDto = eventJobRepository.findInstrumentIdAndEventCepByIdAndUserId(eventJobId, userId) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Você não pode buscar um músico para este evento")
        val musiciansEventJobDto = musicianService.findMusicianEventJobDtoByInstrumentId(instrumentIdAndEventCepDto.instrumentId, filterMusicianRequest, pageable)
        var destinations: String = ""

        if(musiciansEventJobDto.isEmpty()) throw ResponseStatusException(HttpStatus.NO_CONTENT, "Nenhum músico foi encontrado")

        musiciansEventJobDto.forEach { destinations+= it.cep + "|" }
        destinations = destinations.dropLast(1)

        val response = googleMapsService.getDistanceMatrix(filterMusicianRequest.cep ?: instrumentIdAndEventCepDto.cep, destinations)
        val mapper = ObjectMapper()
        val data = mapper.readValue(response, Map::class.java)

        val rows = data["rows"] as List<*>

        musiciansEventJobDto.content.forEach {
            for ((rowIndex, row) in rows.withIndex()) {
                if (row is Map<*, *>) {
                    val elements = row["elements"] as List<*>
                    for ((elementIndex, element) in elements.withIndex()) {
                        if (element is Map<*, *> && element["status"] as? String == "OK") {
                            val distance = (element["distance"] as Map<String, Any>)["value"] as Int
                            it.distance = distance
                        }
                    }
                }
            }
        }

       return musiciansEventJobDto
    }

    fun findEventsByOrganizer(req: HttpServletRequest): List<com.example.authenticationservice.application.web.dto.response.CalendarEventDto> {
        val token  = jwtTokenProvider.resolveToken(req) ?: throw ResponseStatusException(HttpStatus.FORBIDDEN, "Tipo de usuário inválido")
        val userId = jwtTokenProvider.getId(token).toLong()

        val events = eventRepository.findCalendarEventsByOganizer(userId)
        if (events.isEmpty()) throw ResponseStatusException(HttpStatus.NOT_FOUND, "Vocẽ não pode aprovar esse job")

        return events
    }
}