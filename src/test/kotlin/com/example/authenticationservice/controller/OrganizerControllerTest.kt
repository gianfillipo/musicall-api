//import com.example.authenticationservice.application.web.controller.OrganizerController
//import com.example.authenticationservice.application.web.controller.dto.response.CreateEventDto
//import com.example.authenticationservice.response.EventJobDto
//import com.example.authenticationservice.application.web.controller.dto.request.CreateEventJobRequest
//import com.example.authenticationservice.application.web.controller.dto.request.CreateEventRequest
//import com.example.authenticationservice.application.web.controller.dto.request.DeleteEventRequest
//import com.example.authenticationservice.application.web.controller.dto.request.UpdateEventRequest
//import com.example.authenticationservice.domain.service.OrganizerService
//import io.mockk.*
//import org.junit.jupiter.api.Assertions.*
//import org.junit.jupiter.api.BeforeEach
//import org.junit.jupiter.api.Test
//import org.springframework.http.HttpStatus
//import javax.servlet.http.HttpServletRequest
//
//class OrganizerControllerTest {
//    private lateinit var organizerService: OrganizerService
//    private lateinit var organizerController: OrganizerController
//
//    @BeforeEach
//    fun setup() {
//        organizerService = mockk(relaxed = true)
//        organizerController = OrganizerController(organizerService)
//    }
//
//    @Test
//    fun `should create a event and return createEventDto`() {
//        val request = mockk<HttpServletRequest>(relaxed = true)
//        val createEventRequest = mockk<com.example.authenticationservice.application.web.controller.dto.request.CreateEventRequest>(relaxed = true)
//
//        val createEventDto = com.example.authenticationservice.application.web.controller.dto.response.CreateEventDto()
//        every { organizerService.createEvent(any(), any()) } returns createEventDto
//
//        val response = organizerController.createEvent(request, createEventRequest)
//
//        assertEquals(HttpStatus.CREATED, response.statusCode)
//        assertEquals(createEventDto, response.body)
//        verify (exactly = 1) {  organizerService.createEvent(any(), any()) }
//    }
//
//    @Test
//    fun `should create a eventJob and return eventJobDto`() {
//        val request: HttpServletRequest = mockk<HttpServletRequest>(relaxed = true)
//        val createEventJobRequest = mockk<com.example.authenticationservice.application.web.controller.dto.request.CreateEventJobRequest>(relaxed = true)
//
//        every { organizerService.createEventJob(any(), any()) } returns listOf<EventJobDto>()
//
//        val response = organizerController.createEventJob(request, createEventJobRequest)
//        val eventJobDto: List<EventJobDto> = listOf<EventJobDto>()
//
//        assertEquals(HttpStatus.CREATED, response.statusCode)
//        assertEquals(eventJobDto, response.body)
//        verify (exactly = 1) { organizerService.createEventJob(any(), any()) }
//    }
//
//    @Test
//    fun `should update event and return createEventDto`() {
//        val request: HttpServletRequest = mockk<HttpServletRequest>(relaxed = true)
//        val createEventJobRequest = mockk<com.example.authenticationservice.application.web.controller.dto.request.UpdateEventRequest>(relaxed = true)
//        val createEventDto = com.example.authenticationservice.application.web.controller.dto.response.CreateEventDto()
//
//        every { organizerService.updateEvent(any(), any()) } returns createEventDto
//
//        val response = organizerController.updateEvent(request, createEventJobRequest)
//
//        assertEquals(HttpStatus.OK, response.statusCode)
//        assertEquals(createEventDto, response.body)
//        verify (exactly = 1) { organizerService.updateEvent(any(), any()) }
//    }
//
//
//    @Test
//    fun `should delete event`() {
//        val request: HttpServletRequest = mockk<HttpServletRequest>(relaxed = true)
//        val deleteEventJobRequest = mockk<com.example.authenticationservice.application.web.controller.dto.request.DeleteEventRequest>(relaxed = true)
//
//        every { organizerService.deleteEvent(any(), any()) } returns Unit
//
//        val response = organizerController.deleteEvent(request, deleteEventJobRequest)
//
//        assertEquals(HttpStatus.OK, response.statusCode)
//        assertEquals(null, response.body)
//
//        verify (exactly = 1) { organizerService.deleteEvent(any(), any()) }
//    }
//
//    @Test
//    fun `should delete event job`() {
//        val request: HttpServletRequest = mockk<HttpServletRequest>(relaxed = true)
//        val eventJobId: Long = 1
//
//        every { organizerService.deleteEventJob(any(), any()) } returns Unit
//
//        val response = organizerController.deleteEventJob(request, eventJobId)
//
//        assertEquals(HttpStatus.OK, response.statusCode)
//        assertEquals(null, response.body)
//
//        verify (exactly = 1) { organizerService.deleteEventJob(any(), any()) }
//    }
//}