package com.example.authenticationservice.domain.repositories

import com.example.authenticationservice.response.EventSearchDto
import com.example.authenticationservice.application.web.controller.dto.request.FilterEventsRequest

interface
EventRepositoryCustom {
    fun findUnfinalizedEventsAfterOrEqual(filterEventsRequest: com.example.authenticationservice.application.web.controller.dto.request.FilterEventsRequest, instrumentsId: List<Long>): List<EventSearchDto>
}
