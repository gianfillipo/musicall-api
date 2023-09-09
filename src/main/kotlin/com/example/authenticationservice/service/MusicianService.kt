package com.example.authenticationservice.service

import com.example.authenticationservice.utils.GoogleMapsUtils
import com.example.authenticationservice.dao.*
import com.example.authenticationservice.dto.*
import com.example.authenticationservice.mapper.MusicianMapper
import com.example.authenticationservice.model.*
import com.example.authenticationservice.model.JobRequest
import com.example.authenticationservice.parameters.*
import com.example.authenticationservice.security.JwtTokenProvider
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import javax.persistence.Tuple
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
    fun registerMusician(registerMusicianRequest: RegisterMusicianRequest, req : HttpServletRequest) : MusicianDto {
        val token  = jwtTokenProvider.resolveToken(req) ?: throw ResponseStatusException(HttpStatus.FORBIDDEN, "Tipo de usuário inválido")
        val id = jwtTokenProvider.getId(token).toLong()
        val user = userRepository.getById(id) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado")
        if(musicianRepository.existsByUser(user)) throw ResponseStatusException(HttpStatus.CONFLICT, "Usuário não existe")

        val musician = Musician(registerMusicianRequest, user)

        musicianRepository.save(musician)

        return musicianMapper.toDto(musician)
    }

    fun registerInstrument(registerInstrumentRequest: RegisterInstrumentRequest, req: HttpServletRequest): List<InstrumentsDto> {
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
    fun getEventsByLocation(filterEventsRequest: FilterEventsRequest, req: HttpServletRequest): List<EventSearchDto> {
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

    fun updateMusician(updateMusicianRequest: UpdateMusicianRequest, req: HttpServletRequest) {
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

    fun createJobRequest(req: HttpServletRequest, createJobRequestRequest: CreateJobRequestRequest) {
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

    fun findMusicianEventJobDtoByInstrumentId(instrumentId: Long, filterMusicianRequest: FilterMusicianRequest, pageable: Pageable): PageImpl<MusicianEventJobDto> {
        val musicians = musicianRepository.findMusicianByIdAndEventLocation(instrumentId, filterMusicianRequest, pageable)

        return musicians
    }

    fun validationRegister(req: HttpServletRequest ,userId: Long): Boolean {
        jwtTokenProvider.resolveToken(req) ?: throw ResponseStatusException(HttpStatus.FORBIDDEN, "Tipo de usuário inválido")

        val result = musicianRepository.validationMusician(userId)
        return result == "1"
    }
}
