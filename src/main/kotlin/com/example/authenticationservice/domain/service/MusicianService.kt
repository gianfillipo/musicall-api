package com.example.authenticationservice.domain.service

import com.example.authenticationservice.application.web.utils.GoogleMapsUtils
import com.example.authenticationservice.domain.entities.*
import com.example.authenticationservice.domain.entities.JobRequest
import com.example.authenticationservice.domain.repositories.*
import com.example.authenticationservice.application.web.dto.response.*
import com.example.authenticationservice.application.config.security.JwtTokenProvider
import com.example.authenticationservice.application.web.dto.response.EventsInfoForMusicianResponse
import com.example.authenticationservice.application.web.dto.mapper.MusicianMapper
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import javax.servlet.http.HttpServletRequest
import kotlin.collections.HashMap

@Service
class MusicianService (
    @Autowired private val userRepository: UserRepository,
    @Autowired private val jwtTokenProvider: JwtTokenProvider,
    @Autowired private val musicianRepository: MusicianRepository,
    @Autowired private val musicianMapper : MusicianMapper,
    @Autowired private val musicianInstrumentRepository: MusicianInstrumentRepository,
    @Autowired private val instrumentRepository: InstrumentRepository,
    @Autowired private val eventJobRepository: EventJobRepository,
    @Autowired private val jobRequestRepository: JobRequestRepository,
    @Autowired private val eventRepository: EventRepository,
    @Autowired private val notificationRepository: NotificationRepository,
    @Autowired private val googleMapsService: GoogleMapsUtils
) {
    fun registerMusician(registerMusicianRequest: com.example.authenticationservice.application.web.dto.request.RegisterMusicianRequest, req : HttpServletRequest) : MusicianDto {
        val token  = jwtTokenProvider.resolveToken(req) ?: throw ResponseStatusException(HttpStatus.FORBIDDEN, "Tipo de usuário inválido")
        val id = jwtTokenProvider.getId(token).toLong()
        val user = userRepository.getById(id) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado")
        if(musicianRepository.existsByUser(user)) throw ResponseStatusException(HttpStatus.CONFLICT, "Usuário não existe")

        val musician = Musician(registerMusicianRequest, user)

        musicianRepository.save(musician)

        return musicianMapper.toDto(musician)
    }

    fun registerInstrument(registerInstrumentRequest: com.example.authenticationservice.application.web.dto.request.RegisterInstrumentRequest, req: HttpServletRequest): List<InstrumentsDto> {
        val token  = jwtTokenProvider.resolveToken(req) ?: throw ResponseStatusException(HttpStatus.FORBIDDEN, "Tipo de usuário inválido")
        val id = jwtTokenProvider.getId(token).toLong()
        val user = userRepository.getById(id) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado")
        val musician : Musician? = musicianRepository.getByUser(user) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Complete seu cadastro como músico")
        val instrumentsOfUser = musicianInstrumentRepository.findByMusician(musician!!).map { it.instrument.id }.toHashSet()

        registerInstrumentRequest.fkInstrument!!.forEach { if (instrumentsOfUser.contains(it)) throw ResponseStatusException(HttpStatus.CONFLICT, "Instruento já foi cadastrado")}

        val instruments = instrumentRepository.findAll()
        val instrumentMap : HashMap<Long, Instrument> = HashMap()
        instruments.forEach { instrumentMap[it.id] = it }

//        instrumentsOfUser.forEach { if (instrumentMap.containsKey(it.instrument.id)) throw ResponseStatusException(HttpStatus.CONFLICT, "The instrument is already registered") }

        val musicianInstruments = registerInstrumentRequest.fkInstrument!!.map {
            if (instrumentMap[it] != null) MusicianInstrument(musician = musician!!, instrument = instrumentMap[it]!!)
            else throw ResponseStatusException(HttpStatus.NOT_FOUND, "Istrumento não encontrado")
        }

        musicianInstrumentRepository.saveAll(musicianInstruments)

        return musicianInstruments.map { InstrumentsDto(it.instrument.id, it.instrument.name) }
    }
    fun getEventsByLocation(filterEventsRequest: com.example.authenticationservice.application.web.dto.request.FilterEventsRequest, req: HttpServletRequest): List<EventSearchDto> {
        val token  = jwtTokenProvider.resolveToken(req) ?: throw ResponseStatusException(HttpStatus.FORBIDDEN, "Tipo de usuário inválido")
        val id = jwtTokenProvider.getId(token).toLong()
        val cep = musicianRepository.findCepByUserId(id) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Complete seu cadastro como músico")
        val instrumentsId = musicianRepository.findInstrumentIdsByUserId(id)
        if (instrumentsId.isEmpty()) throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Você precisa adicionar algum instrumento primeiro")

        var events = eventRepository.findUnfinalizedEventsAfterOrEqual(filterEventsRequest, instrumentsId)

        events.forEach { println(it.eventDate) }
        events.forEach { println(it.id) }

        if (events.isEmpty()) throw ResponseStatusException(HttpStatus.NO_CONTENT, "Nenhum evento foi encontrado")

        var destinations: String = ""
        events.forEach { destinations+=it.cep + "|"}
        destinations = destinations.dropLast(1)

        val response = googleMapsService.getDistanceMatrix(filterEventsRequest.cep ?: cep, destinations)
        val mapper = ObjectMapper()
        val data = mapper.readValue(response, Map::class.java)
        val eventsDto = events

        val rows = data["rows"] as List<*>
        for ((rowIndex, row) in rows.withIndex()) {
            if (row is Map<*, *>) {
                val elements = row["elements"] as List<*>
                for ((elementIndex, element) in elements.withIndex()) {
                    if (element is Map<*, *> && element["status"] as? String == "OK") {
                        val distance = (element["distance"] as Map<String, Any>)["value"] as Int
                        val address = (data["destination_addresses"] as List<String>)
                        eventsDto[elementIndex].cep =  address[elementIndex]
                        eventsDto[elementIndex].distance = distance
                    }
                }
            }
        }

        return eventsDto.sortedBy { it.distance }
    }

    fun updateMusician(updateMusicianRequest: com.example.authenticationservice.application.web.dto.request.UpdateMusicianRequest, req: HttpServletRequest) {
        val token  = jwtTokenProvider.resolveToken(req) ?: throw ResponseStatusException(HttpStatus.FORBIDDEN, "Tipo de usuário inválido")
        val id = jwtTokenProvider.getId(token).toLong()
        val musician = musicianRepository.getMusicianByUserId(id) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado")

        if (updateMusicianRequest.cep.isNullOrBlank() and updateMusicianRequest.description.isNullOrBlank()) throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Faltam informações")

        var hasChanges = false

        if (updateMusicianRequest.cep != null) {
            musician.cep = updateMusicianRequest.cep!!
            hasChanges = true
        }

        if (updateMusicianRequest.description != null) {
            musician.description = updateMusicianRequest.description!!
            hasChanges = true
        }

        if (updateMusicianRequest.imageUrl != null) {
            musician.imageUrl = updateMusicianRequest.imageUrl!!
            hasChanges = true
        }

        if (!hasChanges) throw ResponseStatusException(HttpStatus.CONFLICT, "As informações são as mesmas")

        musicianRepository.save(musician)
    }

    fun createJobRequest(req: HttpServletRequest, createJobRequestRequest: com.example.authenticationservice.application.web.dto.request.CreateJobRequestRequest) {
        val token  = jwtTokenProvider.resolveToken(req) ?: throw ResponseStatusException(HttpStatus.FORBIDDEN, "Tipo de usuário inváido")
        val id = jwtTokenProvider.getId(token).toLong()
        val musician = musicianRepository.findByUserId(id) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Complete seu cadastro como músico")
        val eventJob = eventJobRepository.getById(createJobRequestRequest.fkEventJob!!) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Vaga não encontrada")

        if (eventJob.musician != null) throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Já existe alguém ocupando essa vaga")

        val musicianInstrumentHash = musician.musicianInstruments.map { it.instrument.id }.toHashSet()
        if (!musicianInstrumentHash.contains(eventJob.instrument.id)) throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Vocẽ não toca esse instrumento")

        val jobRequest = JobRequest(eventJob = eventJob, musician = musician, musicianConfirmed = true)
        if (jobRequestRepository.existsByMusicianIdAndEventId(musician.id, eventJob.event.id)) throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Você já fez uma solicitação para essa vaga")
        if (eventJobRepository.existsByEventDateAndMusicianId(eventJob.event.eventDate, musician.id)) throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Você já tem um evento nessa data")


        jobRequestRepository.save(jobRequest)
        notificationRepository.save(Notification(jobRequest = jobRequest, user = eventJob.event.user, notificationType = NotificationTypeDto.REQUEST))
    }

    @Transactional
    fun deleteJobRequest(req: HttpServletRequest, jobRequestId: Long) {
        val token  = jwtTokenProvider.resolveToken(req) ?: throw ResponseStatusException(HttpStatus.FORBIDDEN, "Tipo de usuário inválido")
        val id = jwtTokenProvider.getId(token).toLong()
        val musicianId = musicianRepository.findIdByUserId(id) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Complete seu cadastro como músico")
        val deleteJobRequestDto = jobRequestRepository.findIdAndOrganizerConfirmedByEventJobIdAndMusicianId(jobRequestId, musicianId) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "You can't delete this job request")

        if (deleteJobRequestDto.organizerConfirmed) throw ResponseStatusException(HttpStatus.BAD_REQUEST, "O organizador já confirmou esse vaga")

        notificationRepository.deleteByJobRequestId(deleteJobRequestDto.id)
        jobRequestRepository.deleteById(deleteJobRequestDto.id)
    }

    fun findMusicianEventJobDtoByInstrumentId(filterMusicianRequest: com.example.authenticationservice.application.web.dto.request.FilterMusicianRequest, pageable: Pageable): PageImpl<MusicianEventJobDto> {
        val musicians = musicianRepository.findMusicianByIdAndEventLocation(filterMusicianRequest, pageable)

        return musicians
    }

    fun validationRegister(req: HttpServletRequest ,userId: Long): Boolean {
        jwtTokenProvider.resolveToken(req) ?: throw ResponseStatusException(HttpStatus.FORBIDDEN, "Tipo de usuário inválido")

        val result = musicianRepository.validationMusician(userId)
        return result == "1"
    }

    fun getEventsById(req: HttpServletRequest, eventId: Long): EventDto? {
        jwtTokenProvider.resolveToken(req) ?: throw ResponseStatusException(HttpStatus.FORBIDDEN, "Tipo de usuário inválido")

        return eventRepository.findEventById(eventId)
    }

    fun findEventsInfoByMusician(req: HttpServletRequest): List<EventsInfoForMusicianResponse> {
        val token = jwtTokenProvider.resolveToken(req) ?: throw ResponseStatusException(HttpStatus.FORBIDDEN, "Tipo de usuário inválido")
        val id = jwtTokenProvider.getId(token).toLong()
        val musician = musicianRepository.findByUserId(id) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não completou cadastro")

        return eventRepository.findEventInfoByMusicianId(musician!!.id)
    }

    fun getEventInfoByEventJobId(eventJobId: Long): EventsInfoResponse? {
        val response = eventRepository.getEventInfoById(eventJobId)?: throw ResponseStatusException(HttpStatus.NO_CONTENT, "Id do evento é inválido")
        val vagas = eventJobRepository.findById(eventJobId)

        response.eventJob = EventJobDto(vagas.get())

        return response
    }

    @Transactional
    fun approveJobRequest(id: Long, jobRequestId: Long) {
        val userId = jobRequestRepository.findOrganizerForNotification(jobRequestId) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Vocẽ não pode aprovar esse job")
        val musician = musicianRepository.findByUserId(id)
        val eventJobId = jobRequestRepository.findEvenJobIdById(jobRequestId) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Vaga não encontrada")

        jobRequestRepository.updateMusicianConfirmedTrueById(jobRequestId, musician!!.id)
        eventJobRepository.setMusicianByEventId(musician, eventJobId!!)


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

    fun getJobRequestByMusicianId(musicianId: Long, req: HttpServletRequest): InvitesKpiMusicianResponse? {
        val token = jwtTokenProvider.resolveToken(req) ?: throw ResponseStatusException(HttpStatus.FORBIDDEN, "Tipo de usuário inválido")
        val id = jwtTokenProvider.getId(token).toLong()
        val musician = musicianRepository.findByUserId(id) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não completou cadastro")

        return musicianRepository.getJobRequestByMusicianId(musicianId)
    }

    fun getAllMatchesByMusiciaId(musicianId: Long, req: HttpServletRequest): TotalMatchsKpiMusician? {
        val token = jwtTokenProvider.resolveToken(req) ?: throw ResponseStatusException(HttpStatus.FORBIDDEN, "Tipo de usuário inválido")
        val id = jwtTokenProvider.getId(token).toLong()
        val musician = musicianRepository.findByUserId(id) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não completou cadastro")

        return musicianRepository.getAllMatchesByMusiciaId(musicianId)
    }

    fun getAllInstrumentsByVacancies(req: HttpServletRequest): List<EventJobPerInstrumentResponse>? {
        val token = jwtTokenProvider.resolveToken(req) ?: throw ResponseStatusException(HttpStatus.FORBIDDEN, "Tipo de usuário inválido")
        val id = jwtTokenProvider.getId(token).toLong()
        val musician = musicianRepository.findByUserId(id) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não completou cadastro")

        val topInstruments = musicianRepository.findTop5InstrumentsByVacancies()

        return if (topInstruments.size > 5) topInstruments.subList(0, 5) else topInstruments

    }

    fun getAllInvitesByInstruments(musicianId: Long, req: HttpServletRequest): List<InvitePerInstrumentResponse>? {
        val token = jwtTokenProvider.resolveToken(req) ?: throw ResponseStatusException(HttpStatus.FORBIDDEN, "Tipo de usuário inválido")
        val id = jwtTokenProvider.getId(token).toLong()
        val musician = musicianRepository.findByUserId(id) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não completou cadastro")

        return musicianRepository.getAllInvitesByInstruments(musicianId)
    }
}
