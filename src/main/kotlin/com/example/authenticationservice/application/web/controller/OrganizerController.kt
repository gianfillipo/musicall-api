package com.example.authenticationservice.application.web.controller

import com.example.authenticationservice.application.web.dto.response.CalendarEventByIdDto
import com.example.authenticationservice.application.web.dto.response.EventJobDto
import com.example.authenticationservice.application.web.dto.response.MusicianEventJobDto
import com.example.authenticationservice.domain.exceptions.ParameterException
import com.example.authenticationservice.domain.service.OrganizerService
import com.sun.istack.NotNull
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.*
import java.util.HashMap
import javax.servlet.http.HttpServletRequest
import javax.validation.Valid


@RestController
@RequestMapping("/org")
@SecurityRequirement(name = "Bearer Authentication")
class OrganizerController (
        @Autowired private val organizerService : OrganizerService
) {
    @PostMapping("/event")
    fun createEvent(req : HttpServletRequest, @Valid @RequestBody createEventRequest: com.example.authenticationservice.application.web.dto.request.CreateEventRequest) : ResponseEntity<com.example.authenticationservice.application.web.dto.response.CreateEventDto> {
        val createEventDto = organizerService.createEvent(createEventRequest, req)

        return ResponseEntity.status(201).body(createEventDto)
    }

    @GetMapping("/event")
    fun findEventsByOrganizer(req : HttpServletRequest) : List<com.example.authenticationservice.application.web.dto.response.CalendarEventDto> {
        val createEventDto = organizerService.findEventsByOrganizer(req)

        return createEventDto
    }


    @GetMapping("/event/{id}")
    fun findEventsByOrganizerByEventId(req : HttpServletRequest, @PathVariable("id") id:Long) : List<CalendarEventByIdDto> {
        val createEventDto = organizerService.findEventsByOrganizerByEventId(req,id)

        return createEventDto
    }

    @PostMapping("/event/job")
    fun createEventJob(req : HttpServletRequest, @Valid @RequestBody createEventJobRequest: com.example.authenticationservice.application.web.dto.request.CreateEventJobRequest) : ResponseEntity<List<EventJobDto>> {
        val eventJobsDto : List<EventJobDto> = organizerService.createEventJob(createEventJobRequest, req)

        return ResponseEntity.status(201).body(eventJobsDto)
    }

    @PutMapping("/event")
    fun updateEvent(req : HttpServletRequest, @Valid @RequestBody updateEventRequest: com.example.authenticationservice.application.web.dto.request.UpdateEventRequest): ResponseEntity<com.example.authenticationservice.application.web.dto.response.CreateEventDto> {
        val eventDto = organizerService.updateEvent(updateEventRequest, req)

        return ResponseEntity.status(200).body(eventDto)
    }
    @DeleteMapping("/event")
    fun deleteEvent(req : HttpServletRequest, @Valid @RequestBody deleteEventRequest: com.example.authenticationservice.application.web.dto.request.DeleteEventRequest): ResponseEntity<Void> {
        organizerService.deleteEvent(req, deleteEventRequest)

        return ResponseEntity.status(200).build()
    }

    @DeleteMapping("/event-job/{id}")
    fun deleteEventJob(req : HttpServletRequest, @PathVariable("id") @Valid @NotNull id: Long?): ResponseEntity<Void> {
        organizerService.deleteEventJob(req, id)

        return ResponseEntity.status(200).build()
    }

    @PostMapping("/musician/event/{eventId}")
    fun findMusicianByEventLocation(
        req: HttpServletRequest,
        @Valid @RequestBody filterMusicianRequest: com.example.authenticationservice.application.web.dto.request.FilterMusicianRequest,
        @PathVariable("eventId") @Valid @NotNull eventId: Long,
        @RequestParam("page", defaultValue = "0") page: Int,
        @RequestParam("size", defaultValue = "10") size: Int
    ) : ResponseEntity<PageImpl<MusicianEventJobDto>> {
        val events = organizerService.findMusicianByEventLocation(req, eventId, filterMusicianRequest, PageRequest.of(page, size))

        return ResponseEntity.status(200).body(events)
    }

    @PostMapping("/job-request")
    fun createJobRequest(req: HttpServletRequest, @RequestParam eventJobId: Long, @RequestParam musicianId: Long) {
        organizerService.createJobRequest(req, eventJobId, musicianId)
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationExceptions(ex: MethodArgumentNotValidException): Map<String, String> {
        val errors = HashMap<String, String>()
        ex.bindingResult.allErrors.forEach { error ->
            val fieldName = (error as FieldError).field
            val errorMessage = error.getDefaultMessage()
            errors[fieldName] = errorMessage ?: "Error"
        }
        return errors
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ParameterException::class)
    fun handleValidationExceptions(ex: ParameterException): Map<String, String> {
        val errors = HashMap<String, String>()
        errors[ex.parameter] = ex.message;
        return errors
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleEmptyBodyException(ex: HttpMessageNotReadableException): Map<String, String> {
        val errors = HashMap<String, String>()
        errors["request body"] = "Request body has an error or is empty"
        return errors
    }
}
