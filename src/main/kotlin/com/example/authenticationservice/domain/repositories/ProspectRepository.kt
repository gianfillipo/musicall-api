package com.example.authenticationservice.domain.repositories

import com.example.authenticationservice.domain.entities.Prospect
import org.springframework.data.jpa.repository.JpaRepository

interface ProspectRepository : JpaRepository<Prospect, Long> {
    fun findByEmail(email: String): Prospect?
}
