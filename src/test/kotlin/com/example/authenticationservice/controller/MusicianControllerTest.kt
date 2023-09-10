package com.example.authenticationservice.controller

import com.example.authenticationservice.application.web.controller.MusicianController
import com.example.authenticationservice.response.*
import com.example.authenticationservice.domain.exceptions.InvalidJwtAuthenticationException
import com.example.authenticationservice.domain.exceptions.ParameterException
import com.example.authenticationservice.application.web.controller.dto.request.FilterEventsRequest
import com.example.authenticationservice.application.web.controller.dto.request.RegisterInstrumentRequest
import com.example.authenticationservice.application.web.controller.dto.request.RegisterMusicianRequest
import com.example.authenticationservice.application.web.controller.dto.request.UpdateMusicianRequest
import com.example.authenticationservice.domain.service.MusicianService
import io.mockk.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus

import javax.servlet.http.HttpServletRequest


class MusicianControllerTest {

    private lateinit var musicianService: MusicianService
    private lateinit var musicianController: MusicianController

    @BeforeEach
    fun setup() {
        musicianService = mockk(relaxed = true)
        musicianController = MusicianController(musicianService)
    }

    @Test
    fun testRegisterMusician() {
        // Arrange
        val request = mockk<HttpServletRequest>(relaxed = true)
        val registerMusicianRequest =
            com.example.authenticationservice.application.web.controller.dto.request.RegisterMusicianRequest(
                "John Doe",
                "john@example.com",
                "password"
            )
        val musicianDto = MusicianDto(String(), String())
        every { musicianService.registerMusician(any(), any()) } returns musicianDto

        // Act
        val response = musicianController.registerMusician(request, registerMusicianRequest)

        // Assert
        assertEquals(HttpStatus.CREATED, response.statusCode)
        assertEquals(musicianDto, response.body)
        verify { musicianService.registerMusician(registerMusicianRequest, request) }
    }

    @Test
    fun testUpdateMusician() {
        // Arrange
        val request = mockk<HttpServletRequest>(relaxed = true)
        val updateMusicianRequest =
            com.example.authenticationservice.application.web.controller.dto.request.UpdateMusicianRequest(
                String(),
                String(),
                String()
            )
        every { musicianService.updateMusician(any(), any()) } just Runs

        // Act
        val response = musicianController.updateMusician(request, updateMusicianRequest)

        // Assert
        assertEquals(HttpStatus.OK, response.statusCode)
        verify { musicianService.updateMusician(updateMusicianRequest, request) }
    }

    @Test
    fun testFindEventsByLocation() {
        // Arrange
        val request = mockk<HttpServletRequest>(relaxed = true)
        val filterEventsRequest = mockk<com.example.authenticationservice.application.web.controller.dto.request.FilterEventsRequest>()
        val eventDto1 = mockk<EventSearchDto>()
        val eventDto2 = mockk<EventSearchDto>()
        val events = listOf(eventDto1, eventDto2)
        every { musicianService.getEventsByLocation(any(), any()) } returns events

        // Act
        val response = musicianController.findEventsByLocation(request, filterEventsRequest)

        // Assert
        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(events, response.body)
        verify { musicianService.getEventsByLocation(filterEventsRequest, request) }
    }

    @Test
    fun testRegisterInstrument() {
        // Arrange
        val request = mockk<HttpServletRequest>(relaxed = true)
        val registerInstrumentRequest =
            com.example.authenticationservice.application.web.controller.dto.request.RegisterInstrumentRequest(listOf(5L))
        val instrumentDto1 = InstrumentsDto(1, "Guitar")
        val instrumentDto2 = InstrumentsDto(2, "Bass")
        val instrumentDtos = listOf(instrumentDto1, instrumentDto2)
        every { musicianService.registerInstrument(any(), any()) } returns instrumentDtos

        // Act
        val response = musicianController.registerInstrument(request, registerInstrumentRequest)

        // Assert
        assertEquals(HttpStatus.CREATED, response.statusCode)
        assertEquals(instrumentDtos, response.body)
        verify { musicianService.registerInstrument(registerInstrumentRequest, request) }
    }

    @Test
    fun testHandleInvalidJwtAuthenticationException() {
        // Arrange
        val exception = InvalidJwtAuthenticationException("Invalid token")

        // Act
        val response = musicianController.handleValidationExceptions(exception)

        // Assert
        assertEquals(1, response.size)
        assertEquals("Invalid token", response["error"])
    }


    @Test
    fun testHandleParameterException() {
        // Arrange
        val exception = ParameterException("parameterName", "Parameter error")

        // Act
        val response = musicianController.handleValidationExceptions(exception)

        // Assert
        assertEquals(1, response.size)
        assertEquals("Parameter error", response["parameterName"])
    }
}
