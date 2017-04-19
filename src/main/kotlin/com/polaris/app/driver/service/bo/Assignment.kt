package com.polaris.app.driver.service.bo

import java.time.LocalDate
import java.time.LocalTime

data class Assignment(
        val assignmentID: Int,
        val serviceID: Int,
        val driverID: Int,
        val shuttleID: Int,
        val routeID: Int,
        val startTime: LocalTime,
        val startDate: LocalDate,
        val routeName: String?,
        val status: String,
        val stops: List<Stop>
)