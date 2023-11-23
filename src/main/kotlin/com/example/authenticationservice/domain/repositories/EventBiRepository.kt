package com.example.authenticationservice.domain.repositories

import EventBi
import org.springframework.data.jpa.repository.JpaRepository

interface EventBiRepository : JpaRepository<EventBi, Long> {
}