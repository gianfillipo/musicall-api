package com.example.authenticationservice.domain.entities

import com.example.authenticationservice.application.web.dto.request.RegisterMusicianRequest
import javax.persistence.*

@Entity
data class Musician(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @OneToMany(mappedBy = "musician", cascade = [CascadeType.ALL], orphanRemoval = true)
    val jobRequests: MutableList<JobRequest> = mutableListOf(),

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn (name = "user_id", nullable = false)
    val user: User,

    @Column(nullable = false)
    var description: String,

    @Column(nullable = false)
    var cep: String,

    @Column(nullable = true)
    var imageUrl: String?,

    @OneToMany(mappedBy = "musician", cascade = [CascadeType.ALL], orphanRemoval = true)
    val musicianInstruments: MutableList<MusicianInstrument> = mutableListOf(),

    @OneToMany(mappedBy = "musician", cascade = [CascadeType.ALL], orphanRemoval = true)
    val eventJob: MutableList<EventJob> = mutableListOf()
){
    constructor() : this(
        user = User(),
        description = "",
        cep = "",
        imageUrl = ""
    )

    constructor(registerMusicianRequest: RegisterMusicianRequest, user: User) : this(
        user = user,
        description = registerMusicianRequest.description!!,
        cep = registerMusicianRequest.cep!!,
        imageUrl = registerMusicianRequest.imageUrl
    )
}