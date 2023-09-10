package com.example.authenticationservice.application.web.dto.response

import com.example.authenticationservice.domain.entities.User

data class UserDto (
    val id: Long,
    val name: String
) {
    constructor(user: User) : this(
            id = user.id,
            name = user.name
    )

    constructor() : this(
            id = 0,
            name = ""
    )
}
