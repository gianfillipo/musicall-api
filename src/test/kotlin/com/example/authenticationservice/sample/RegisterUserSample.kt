//package com.example.authenticationservice.sample
//
//import com.example.authenticationservice.response.TypeUserDto
//import com.example.authenticationservice.application.web.controller.dto.request.RegisterUserRequest
//import java.time.LocalDate
//
//object RegisterUserSample {
//    fun getRegisterUserSuccess(): com.example.authenticationservice.application.web.controller.dto.request.RegisterUserRequest =
//        com.example.authenticationservice.application.web.controller.dto.request.RegisterUserRequest(
//            type = TypeUserDto.MSC,
//            name = "John Doe",
//            cpf = "12345678901",
//            birthDate = LocalDate.of(1990, 5, 15),
//            telephone = "1234567890",
//            email = "johndoe@example.com",
//            password = "secretpassword"
//        )
//
//    fun getRegisterUserWithInvalidType(): com.example.authenticationservice.application.web.controller.dto.request.RegisterUserRequest =
//        com.example.authenticationservice.application.web.controller.dto.request.RegisterUserRequest(
//            type = null,
//            name = "John Doe",
//            cpf = "12345678901",
//            birthDate = LocalDate.of(1990, 5, 15),
//            telephone = "1234567890",
//            email = "johndoe@example.com",
//            password = "secretpassword"
//        )
//}