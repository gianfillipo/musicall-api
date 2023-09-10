package com.example.authenticationservice.application.web.dto.response

data class PageMusicianEventJobDto (
    val content: List<MusicianEventJobDto>,
    val pageNo: Int,
    val pageSize: Int,
    val totalElements: Int,
    val totalPages: Int,
    val last: Boolean
)

