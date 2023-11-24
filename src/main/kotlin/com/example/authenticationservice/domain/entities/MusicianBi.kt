package com.example.authenticationservice.domain.entities

import javax.persistence.*

@Entity
data class MusicianBi(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = true)
    var region: String?,

    @Column(nullable = true)
    var state: String?,
) {
}