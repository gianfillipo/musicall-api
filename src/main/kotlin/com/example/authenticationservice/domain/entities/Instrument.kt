package com.example.authenticationservice.domain.entities

import javax.persistence.*

@Entity
data class Instrument (
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long = 0,

        @Column(nullable = false)
        val name: String,

        @OneToMany(mappedBy = "instrument")
        val musicianInstruments: MutableList<MusicianInstrument> = mutableListOf(),

        @OneToMany(mappedBy = "instrument")
        val eventJob: MutableList<EventJob> = mutableListOf()
) {
    constructor (): this (
            name = ""
    )
}
