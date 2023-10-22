package com.example.authenticationservice.application.web.controller

import com.example.authenticationservice.application.web.dto.response.InstrumentDto
import com.example.authenticationservice.application.web.dto.response.JobRequestDto
import com.example.authenticationservice.application.web.dto.response.TypeUserDto
import com.example.authenticationservice.domain.exceptions.InvalidJwtAuthenticationException
import com.example.authenticationservice.domain.exceptions.ParameterException
import com.example.authenticationservice.application.web.dto.request.DeleteUserRequest
import com.example.authenticationservice.application.web.dto.request.EmailResetRequest
import com.example.authenticationservice.application.web.dto.request.SetEmailRequest
import com.example.authenticationservice.application.web.dto.response.MusicianInfoResponse
import com.example.authenticationservice.domain.service.UserService
import com.sun.istack.NotNull
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.*
import java.util.HashMap
import javax.servlet.http.HttpServletRequest
import javax.validation.Valid
import javax.validation.constraints.NotBlank

@RestController
@RequestMapping("/usr")
@SecurityRequirement(name = "Bearer Authentication")
class UserController (
        @Autowired private val userService : UserService
) {
    @GetMapping("/auth")
    fun isAuthenticated(req: HttpServletRequest): ResponseEntity<TypeUserDto> {
        val userType = userService.findType(req)

        return ResponseEntity.status(200).body(userType)
    }
    @DeleteMapping
    fun deleteUser(req: HttpServletRequest, @RequestBody @Valid deleteUserRequest: com.example.authenticationservice.application.web.dto.request.DeleteUserRequest) : ResponseEntity<Void> {
        val userDto = userService.deleteUser(req, deleteUserRequest)

        return ResponseEntity.status(200).build()
    }
    @PostMapping("/change-email")
    fun requestEmailReset(req: HttpServletRequest, @Valid @RequestBody setEmailRequest: com.example.authenticationservice.application.web.dto.request.EmailResetRequest): ResponseEntity<Void> {
        val resetToken = userService.requestEmailReset(req, setEmailRequest)
        /*emailSenderService.sendEmail(
            "${setEmailRequest.email}",
            "Código para troca de email",
            "${resetToken}"
        )*/

        return ResponseEntity.status(200).build()
    }


    @PatchMapping("/change-email")
    fun setNewEmail(req: HttpServletRequest, @Valid @NotBlank @NotNull @RequestBody setEmailRequest: com.example.authenticationservice.application.web.dto.request.SetEmailRequest): ResponseEntity<Void> {
        userService.setNewEmail(req, setEmailRequest)

        return ResponseEntity.status(200).build()
    }
    @GetMapping("/event/job-request")
    fun findJobsNotification(req: HttpServletRequest): ResponseEntity<List<JobRequestDto>> {
        val jobRequests = userService.findJobsNotification(req)

        return ResponseEntity.status(200).body(jobRequests)
    }

    @DeleteMapping("/event/job-request/{id}")
    fun deleteJobsNotification(req: HttpServletRequest, @PathVariable("id") @Valid @NotNull id: Long?): ResponseEntity<Void> {
        userService.deleteJobNotification(req, id)

        return ResponseEntity.status(200).build()
    }

    @PostMapping("/event/job-request/{id}")
    fun approveJobRequest(req: HttpServletRequest, @PathVariable("id") @Valid @NotNull id: Long?): ResponseEntity<Void> {
        userService.approveJobRequest(req, id)

        return ResponseEntity.status(200).build()
    }

    @GetMapping("/instruments")
    fun getInstruments(req: HttpServletRequest): ResponseEntity<List<InstrumentDto>>{
        val result = userService.getInstruments(req)

        return ResponseEntity.status(200).body(result)
    }

    @GetMapping("/msc/{musicianId}")
    fun getMusicianInfo(req: HttpServletRequest, @PathVariable musicianId: Long): ResponseEntity<MusicianInfoResponse>{
        val result = userService.getMusicianInfo(req, musicianId)

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