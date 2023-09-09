package com.example.authenticationservice.dao

import com.example.authenticationservice.dto.InstrumentDto
import com.example.authenticationservice.dto.TypeUserDto
import com.example.authenticationservice.model.Instrument
import com.example.authenticationservice.model.User
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
