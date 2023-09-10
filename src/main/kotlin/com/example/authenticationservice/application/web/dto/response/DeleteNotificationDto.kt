package com.example.authenticationservice.application.web.dto.response

data class DeleteNotificationDto (
    val notificationType: com.example.authenticationservice.application.web.controller.dto.response.NotificationTypeDto,
    val fkJobRequest: Long,
    val fkUser: Long
) {
}
