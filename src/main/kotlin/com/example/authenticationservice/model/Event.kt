package com.example.authenticationservice.model

import com.example.authenticationservice.parameters.CreateEventRequest
import java.time.LocalDate
import javax.persistence.*

@Entity
data class Event
  (
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long = 0,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn (name = "user_id", nullable = false)
        val user: User,

        @Column(nullable = false)
        val name: String,

        @Column(nullable = false)
        val local: String,

        @Column(nullable = false)
        val eventDate: LocalDate,

        @Column(nullable = false)
        val durationHours : Int,

        @Column(nullable = false)
        val salary : Float,

        @OneToMany(mappedBy = "event")
        val eventJob: MutableList<EventJob> = mutableListOf()
    ) {
        @Column(nullable = false)
        val finalized = false

        constructor(
            createEventRequest: CreateEventRequest, creator: User
        ) : this(
                user = creator,
                name =  createEventRequest.name,
                local =  createEventRequest.local,
                eventDate =  createEventRequest.eventDate,
                durationHours =  createEventRequest.durationHours,
                salary =  createEventRequest.salary
        )

        constructor() :this (
                user = User(),
                name =  "",
                local =  "",
                eventDate =  LocalDate.now(),
                durationHours =  0,
                salary =  0f
        )
    }
