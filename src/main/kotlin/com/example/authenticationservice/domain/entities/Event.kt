package com.example.authenticationservice.domain.entities

import com.example.authenticationservice.application.web.controller.dto.request.CreateEventRequest
import java.sql.Time
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
        var name: String,

    @Column(nullable = false)
        var aboutEvent: String,

    @Column(nullable = false)
        var cep: String,

    @Column(nullable = false)
        var number: Int,

    @Column(nullable = false)
        var complement: String,

    @Column(nullable = false)
        var eventDate: LocalDate,

    @Column(nullable = false)
        var startHour: Time,

    @Column(nullable = false)
        var durationHours : Int,

    @Column(nullable = false)
        var imageUrl : String,

    @OneToMany(mappedBy = "event", cascade = [CascadeType.ALL], orphanRemoval = true)
        val eventJob: MutableList<EventJob> = mutableListOf()
    ) {
        @Column(nullable = false)
        var finalized = false

        constructor(
            createEventRequest: com.example.authenticationservice.application.web.controller.dto.request.CreateEventRequest, creator: User
        ) : this(
                user = creator,
                name =  createEventRequest.name!!,
                aboutEvent = createEventRequest.aboutEvent!!,
                cep =  createEventRequest.cep!!,
                number = createEventRequest.number!!,
                complement = createEventRequest.complement!!,
                eventDate =  createEventRequest.eventDate!!,
                startHour =  createEventRequest.startHour!!,
                durationHours =  createEventRequest.durationHours!!,
                imageUrl = createEventRequest.imageUrl!!
        )

        constructor(id: Long) : this (
            id = id,
            user = User(),
            name =  "",
            aboutEvent = "",
            cep =  "",
            number = 0,
            complement = "",
            eventDate =  LocalDate.now(),
            startHour = Time(0),
            durationHours =  0,
            imageUrl = ""
        )
        constructor() : this (
                user = User(),
                name =  "",
                aboutEvent = "",
                cep =  "",
                number = 0,
                complement = "",
                eventDate =  LocalDate.now(),
                startHour = Time(0),
                durationHours =  0,
                imageUrl = ""
        )
    }
