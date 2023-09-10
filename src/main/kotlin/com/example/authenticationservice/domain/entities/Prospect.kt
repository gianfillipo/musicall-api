package com.example.authenticationservice.domain.entities

import javax.persistence.*

@Entity
data class Prospect (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    val email: String,

    @Column(nullable = false)
    val name: String,

    @Column(nullable = false)
    val telefone: String,

    @Column(nullable = false)
    val midia: String
) {
    constructor() : this (
            id = 0,
            name = "",
            email = "",
            telefone = "",
            midia = ""
    )
}