//import com.example.authenticationservice.application.web.controller.AuthenticationController
//import com.example.authenticationservice.domain.exceptions.ParameterException
//import com.example.authenticationservice.domain.entities.Prospect
//import com.example.authenticationservice.application.web.controller.dto.request.SetPasswordRequest
//import com.example.authenticationservice.domain.service.AuthenticationService
//import com.example.authenticationservice.domain.service.EmailSenderService
//import com.example.authenticationservice.domain.service.ProspectService
//import io.mockk.*
//import org.junit.jupiter.api.Assertions.assertEquals
//import org.junit.jupiter.api.BeforeEach
//import org.junit.jupiter.api.Test
//import org.springframework.http.HttpStatus
//import javax.validation.Validation
//import javax.validation.Validator
//import javax.validation.ValidatorFactory
//
//
//
//class AuthenticationControllerTest {
//
//    private lateinit var authenticationService: AuthenticationService
//    private lateinit var emailSenderService: EmailSenderService
//    private lateinit var prospectService: ProspectService
//    private lateinit var authenticationController: AuthenticationController
//    private lateinit var validator: Validator
//
//    @BeforeEach
//    fun setup() {
//        authenticationService = mockk()
//        emailSenderService = mockk()
//        prospectService = mockk()
//        authenticationController = AuthenticationController(authenticationService, emailSenderService, prospectService)
//        val validatorFactory: ValidatorFactory = Validation.buildDefaultValidatorFactory()
//        validator = validatorFactory.validator
//    }
//
//
//
//    @Test
//    fun testConfirmUser_ValidRequest_ReturnsOk() {
//        // Arrange
//        val email = "john.doe@example.com"
//        val token = "token"
//        every { authenticationService.confirmUser(email, token) } just runs
//
//        // Act
//        val response = authenticationController.confirmUser(email, token)
//
//        // Assert
//        assertEquals(HttpStatus.OK, response.statusCode)
//        verify(exactly = 1) {
//            authenticationService.confirmUser(email, token)
//        }
//        confirmVerified(authenticationService)
//    }
//
//
//
//    @Test
//    fun testResetPassword_ValidRequest_ReturnsOk() {
//        // Arrange
//        val setPasswordRequest =
//            com.example.authenticationservice.application.web.controller.dto.request.SetPasswordRequest(
//                "password",
//                "token"
//            )
//        every {
//            authenticationService.resetPassword(
//                setPasswordRequest.password,
//                setPasswordRequest.token
//            )
//        } just runs
//
//        // Act
//        val response = authenticationController.resetPassword(setPasswordRequest)
//
//        // Assert
//        assertEquals(HttpStatus.OK, response.statusCode)
//        verify(exactly = 1) {
//            authenticationService.resetPassword(
//                setPasswordRequest.password,
//                setPasswordRequest.token
//            )
//        }
//        confirmVerified(authenticationService)
//    }
//
//    @Test
//    fun testFindProspect_ValidRequest_ReturnsProspect() {
//        // Arrange
//        val email = "john.doe@example.com"
//        val prospect = mockk<Prospect>(email)
//        every { prospectService.findProspect(email) } returns prospect
//
//        // Act
//        val response = authenticationController.findProspect(email)
//
//        // Assert
//        assertEquals(HttpStatus.OK, response.statusCode)
//        assertEquals(prospect, response.body)
//        verify(exactly = 1) {
//            prospectService.findProspect(email)
//        }
//        confirmVerified(prospectService)
//    }
//
//    @Test
//    fun testHandleValidationExceptions_ParameterException_ReturnsBadRequest() {
//        // Arrange
//        val ex = ParameterException("parameter", "message")
//
//        // Act
//        val result = authenticationController.handleValidationExceptions(ex)
//
//        // Assert
//        assertEquals(1, result.size)
//        assertEquals("message", result["parameter"])
//    }
//
//
//}
//
//
