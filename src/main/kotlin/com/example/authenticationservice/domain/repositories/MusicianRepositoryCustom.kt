package com.example.authenticationservice.domain.repositories

import com.example.authenticationservice.application.web.dto.request.FilterMusicianRequest
import com.example.authenticationservice.application.web.dto.response.MusicianEventJobDto
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable

interface MusicianRepositoryCustom {
    fun findMusicianByIdAndEventLocation(filterMusicianRequest: com.example.authenticationservice.application.web.dto.request.FilterMusicianRequest, pageable: Pageable): PageImpl<MusicianEventJobDto>

}
