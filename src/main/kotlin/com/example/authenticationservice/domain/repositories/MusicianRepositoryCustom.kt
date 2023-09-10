package com.example.authenticationservice.domain.repositories

import com.example.authenticationservice.response.MusicianEventJobDto
import com.example.authenticationservice.application.web.controller.dto.request.FilterMusicianRequest
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable

interface MusicianRepositoryCustom {
    fun findMusicianByIdAndEventLocation(instrumentId: Long, filterMusicianRequest: com.example.authenticationservice.application.web.controller.dto.request.FilterMusicianRequest, pageable: Pageable): PageImpl<MusicianEventJobDto>

}
