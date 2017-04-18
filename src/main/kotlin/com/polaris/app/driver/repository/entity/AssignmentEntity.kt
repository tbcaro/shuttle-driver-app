package com.polaris.app.driver.repository.entity

import java.time.LocalDate
import java.time.LocalTime

data class AssignmentEntity(
        val assignmentID: Int,
        val serviceID: Int,
        val driverID: Int,
        val shuttleID: Int,
        val routeID: Int,
        val startTime: LocalTime,
        val startDate: LocalDate,
        val routeName: String?,
        val status: String
)