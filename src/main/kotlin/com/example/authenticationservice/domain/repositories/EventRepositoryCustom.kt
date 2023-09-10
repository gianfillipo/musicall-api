package com.example.authenticationservice.domain.repositories

import com.example.authenticationservice.application.web.dto.response.EventSearchDto


interface
EventRepositoryCustom {
    fun findUnfinalizedEventsAfterOrEqual(filterEventsRequest: com.example.authenticationservice.application.web.dto.request.FilterEventsRequest, instrumentsId: List<Long>): List<EventSearchDto>
}
