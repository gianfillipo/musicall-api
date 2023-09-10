package com.example.authenticationservice.application.web.controller

import com.example.authenticationservice.domain.exceptions.ParameterException
import com.example.authenticationservice.domain.entities.Prospect
import com.example.authenticationservice.domain.service.AuthenticationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import javax.validation.Valid
import org.springframework.validation.FieldError
import java.util.HashMap
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.http.HttpStatus
import com.example.authenticationservice.domain.service.EmailSenderService
import com.example.authenticationservice.domain.service.ProspectService
import com.sun.istack.NotNull
import org.springframework.http.ResponseEntity
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size


@RestController
@RequestMapping("/api")
class AuthenticationController(
    @Autowired private val authenticationService: AuthenticationService,
    @Autowired private val emailSenderService : EmailSenderService,
    @Autowired private val prospectService : ProspectService
) {

    @PostMapping("/register")
    fun registerUser(@Valid @RequestBody registerUserRequest: com.example.authenticationservice.application.web.dto.request.RegisterUserRequest): ResponseEntity<Void> {
        val token = authenticationService.registerUser(registerUserRequest)
        /*emailSenderService.sendEmail(
            "${registerUserRequest.email}",
            "Email de confirmação",
            "http://localhost:8080/api/register/${registerUserRequest.email}/${token}"
        )*/

        return ResponseEntity.status(201).build()
    }

    @GetMapping("/register/{email}/{token}")
    fun confirmUser(@Valid @Email @NotBlank @PathVariable("email") email: String, @Valid @NotBlank @Size(min = 36, max = 36) @PathVariable("token") token: String): ResponseEntity<Void> {
        authenticationService.confirmUser(email, token)

        return ResponseEntity.status(200).build()
   }

    @PostMapping("/login")
    fun login(@RequestBody @Valid data: com.example.authenticationservice.application.web.dto.request.AuthenticationRequest): ResponseEntity<*> {
        val model = authenticationService.login(data.email!!, data.password!!, data.type!!)

        return ResponseEntity.ok(model)
    }


    @PostMapping("/password_reset")
    fun requestPasswordReset(@Valid @RequestBody passwordResetRequest: com.example.authenticationservice.application.web.dto.request.PasswordResetRequest): ResponseEntity<Void> {
        val resetToken = authenticationService.requestPasswordReset(passwordResetRequest.email)
        /*emailSenderService.sendEmail(
            "${passwordResetRequest.email}",
            "Código para a troca da senha",
            "${resetToken}"
        )*/

        return ResponseEntity.status(200).build()
    }

    @PutMapping("/password_reset")
    fun resetPassword(@Valid @RequestBody setPasswordRequest: com.example.authenticationservice.application.web.dto.request.SetPasswordRequest): ResponseEntity<Void> {
        authenticationService.resetPassword(setPasswordRequest.password, setPasswordRequest.token);

        return ResponseEntity.status(200).build()
    }

    @GetMapping("/prospect/{email}")
    fun findProspect(@Valid @NotNull @Email @PathVariable email: String): ResponseEntity<Prospect> {
        val prospect = prospectService.findProspect(email)

        return ResponseEntity.status(200).body(prospect)
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
