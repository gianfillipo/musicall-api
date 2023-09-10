package com.example.authenticationservice.domain.repositories

import com.example.authenticationservice.domain.entities.User

interface UsersDao {
    fun addUser(user: User)
    fun getUserByEmail(email: String): User?
    fun getAllUsers(): List<User>
    fun getAllNotConfirmedUsers(): List<User>
    fun getAllConfirmedUsers(): List<User>
    fun getAllPasswordRequestedUsers(): List<User>
    fun userExists(email: String): Boolean
}