package com.example.authenticationservice.dao

import com.example.authenticationservice.model.Event
import org.springframework.data.jpa.repository.JpaRepository

interface EventRepository : JpaRepository<Event, Long> {
}