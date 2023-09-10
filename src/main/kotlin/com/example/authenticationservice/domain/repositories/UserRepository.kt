package com.example.authenticationservice.domain.repositories

import com.example.authenticationservice.response.InstrumentDto
import com.example.authenticationservice.response.TypeUserDto
import com.example.authenticationservice.domain.entities.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface UserRepository : JpaRepository<User, Long> {
    fun existsByEmailOrCpfOrTelephone(email: String, cpf: String, telephone: String): Boolean
    fun getUserByEmail(email : String) : User?
    fun getUserByEmailAndType(email : String, type : TypeUserDto) : User?
    fun getById(id : Long) : User?
    fun getByPasswordResetToken(passwordResetToken: String) : User?
    @Query("SELECT new com.example.authenticationservice.dto.InstrumentDto(i.id, i.name) FROM Instrument i")
    fun findAllInstrument(): List<InstrumentDto>
}
