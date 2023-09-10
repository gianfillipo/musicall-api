package com.example.authenticationservice.domain.exceptions

import org.springframework.security.core.AuthenticationException

class InvalidJwtAuthenticationException(e: String) : AuthenticationException(e)