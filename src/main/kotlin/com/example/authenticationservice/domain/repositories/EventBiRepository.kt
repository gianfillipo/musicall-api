package com.example.authenticationservice.domain.repositories

import com.example.authenticationservice.domain.entities.EventBi
import org.springframework.data.jpa.repository.JpaRepository

interface EventBiRepository: JpaRepository<EventBi, Long>{

}