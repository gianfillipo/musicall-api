package com.example.authenticationservice.domain.entities

import com.example.authenticationservice.response.TypeUserDto
import com.example.authenticationservice.application.web.controller.dto.request.RegisterUserRequest
import org.mindrot.jbcrypt.BCrypt
import java.time.LocalDate
import java.util.*
import javax.persistence.*

@Entity
data class User(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long = 0,

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        val type: TypeUserDto,

        @Column(nullable = false)
        val name: String,

        @Column(nullable = false, unique = true)
        val cpf: String,

        @Column(nullable = false)
        val birthDate: LocalDate,

        @Column(nullable = false, unique = true)
        val telephone: String,

        @Column(nullable = false, unique = true)
        var email: String,

        @Column(nullable = false)
        var password: String,

        @Column(nullable = false)
        var confirmationToken: String,

        @OneToOne(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
        @JoinColumn(name = "musician_id", referencedColumnName = "id")
        val musician : Musician? = null,

        @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
        val events: MutableList<Event> = mutableListOf(),

        @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
        val notifications: MutableList<Notification> = mutableListOf()
) {
    var numberOfEvents = 0
    var isConfirmed = true
    var isPasswordResetRequested = false
    var passwordResetToken = ""
    var newEmail = ""


    constructor(
            registerUserRequest: com.example.authenticationservice.application.web.controller.dto.request.RegisterUserRequest, token: String
    ) : this(
            type = registerUserRequest.type!!,
            name = registerUserRequest.name!!,
            cpf = registerUserRequest.cpf!!,
            birthDate = registerUserRequest.birthDate!!,
            telephone = registerUserRequest.telephone!!,
            email = registerUserRequest.email!!,
            password = BCrypt.hashpw(registerUserRequest.password!!, BCrypt.gensalt()),
            confirmationToken = token

    )

    constructor() : this(
            type = TypeUserDto.NONE,
            name = "",
            cpf = "",
            birthDate = LocalDate.now(),
            telephone = "",
            email = "",
            password = "",
            confirmationToken = ""
    )
}
