package com.example.authenticationservice.domain.repositories

import com.example.authenticationservice.domain.entities.MusicianBi
import org.springframework.data.jpa.repository.JpaRepository

interface MusicianBiRepository: JpaRepository<MusicianBi, Long> {
}