package com.example.authenticationservice.application.web.controller

import com.example.authenticationservice.application.web.dto.response.*
import com.example.authenticationservice.domain.exceptions.InvalidJwtAuthenticationException
import com.example.authenticationservice.domain.exceptions.ParameterException
import com.example.authenticationservice.application.web.dto.request.*
import com.example.authenticationservice.domain.service.MusicianService
import com.sun.istack.NotNull
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.*
import java.util.HashMap

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest
import javax.validation.Valid


@RestController
@RequestMapping("/msc")
@SecurityRequirement(name = "Bearer Authentication")
class MusicianController (
    @Autowired private val musicianService: MusicianService
) {
    @PostMapping("/register")
    fun registerMusician(req : HttpServletRequest, @Valid @RequestBody registerMusicianRequest: com.example.authenticationservice.application.web.dto.request.RegisterMusicianRequest): ResponseEntity<MusicianDto> {
        val musicianDto = musicianService.registerMusician(registerMusicianRequest, req)
        /*emailSenderService.sendEmail(
            "${registerUserRequest.email}",
            "Email de confirmação",
            "http://localhost:8080/api/register/${registerUserRequest.email}/${token}"
        )*/

        return ResponseEntity.status(201).body(musicianDto)
    }
    @PutMapping("")
    fun updateMusician(req : HttpServletRequest, @Valid @RequestBody updateMusicianRequest: com.example.authenticationservice.application.web.dto.request.UpdateMusicianRequest): ResponseEntity<Void> {
        val eventDto = musicianService.updateMusician(updateMusicianRequest, req)

        return ResponseEntity.status(200).build()
    }

    @PostMapping("/instrument")
    fun registerInstrument(req: HttpServletRequest, @RequestBody @Valid registerInstrumentRequest: com.example.authenticationservice.application.web.dto.request.RegisterInstrumentRequest): ResponseEntity<List<InstrumentsDto>> {
        val instrumentDto = musicianService.registerInstrument(registerInstrumentRequest, req)

        return ResponseEntity.status(201).body(instrumentDto)
    }

    @PostMapping("/event")
    fun findEventsByLocation(req: HttpServletRequest, @RequestBody @Valid filterEventsRequest: com.example.authenticationservice.application.web.dto.request.FilterEventsRequest) : ResponseEntity<List<EventSearchDto>> {
        val events = musicianService.getEventsByLocation(filterEventsRequest, req)

        return ResponseEntity.status(200).body(events)
    }

    @PostMapping("/event/job-request")
    fun createJobRequest(req: HttpServletRequest, @RequestBody @Valid createJobRequestRequest: com.example.authenticationservice.application.web.dto.request.CreateJobRequestRequest): ResponseEntity<Void> {
        musicianService.createJobRequest(req, createJobRequestRequest)

        return ResponseEntity.status(201).build()
    }

    @DeleteMapping("/event/job-request/{jobRequestId}")
    fun deleteJobRequest(req: HttpServletRequest, @PathVariable("jobRequestId") @Valid @NotNull jobRequestId: Long): ResponseEntity<Void> {
        musicianService.deleteJobRequest(req, jobRequestId)

        return ResponseEntity.status(200).build()
    }

    @GetMapping("/validation-register/{userId}")
    fun validationRegister(req: HttpServletRequest, @PathVariable("userId") @NotNull userId: Long): ResponseEntity<Boolean> {
        val result = musicianService.validationRegister(req, userId)

        return ResponseEntity.status(200).body(result)
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(InvalidJwtAuthenticationException::class)
    fun handleValidationExceptions(ex: InvalidJwtAuthenticationException): Map<String, String> {
        val errors = HashMap<String, String>()
        errors["error"] = ex.message.orEmpty()
        return errors
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
}