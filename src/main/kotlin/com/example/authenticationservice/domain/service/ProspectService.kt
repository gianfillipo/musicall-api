package com.example.authenticationservice.domain.service

import com.example.authenticationservice.domain.repositories.ProspectRepository
import com.example.authenticationservice.domain.entities.Prospect
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class ProspectService (
        @Autowired private val prospectRepository: ProspectRepository
){
    fun findProspect(email: String?): Prospect {
        val prospect = prospectRepository.findByEmail(email!!)  ?: throw ResponseStatusException(HttpStatus.NO_CONTENT, "No prospect was found")

        return prospect
    }

}
