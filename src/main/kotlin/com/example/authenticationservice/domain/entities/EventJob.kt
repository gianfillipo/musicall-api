package com.example.authenticationservice.domain.entities

import javax.persistence.*

@Entity
data class EventJob (
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long = 0,

        @OneToMany(mappedBy = "eventJob", cascade = [CascadeType.ALL], orphanRemoval = true)
        val jobRequests: MutableList<JobRequest> = mutableListOf(),

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "event_id", nullable = false)
        val event : Event,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "instrument_id", nullable = false)
        val instrument: Instrument,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "musician_id", nullable = true)
        var musician: Musician? = null,

        @Column(nullable = false)
        val payment: Double = 0.0
) {
    constructor(event: Event, instrument: Instrument, payment: Double) : this (
            event = event,
            instrument = instrument,
            musician = null,
            payment = payment
    )

    constructor() : this (
            event = Event(),
            instrument = Instrument(),
            musician = null
    )
}