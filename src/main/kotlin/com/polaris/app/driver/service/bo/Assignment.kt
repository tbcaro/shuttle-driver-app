package com.polaris.app.driver.service.bo

import com.polaris.app.driver.controller.adapter.enums.AssignmentStatus
import java.time.LocalDate
import java.time.LocalTime

data class Assignment(
        val assignmentID: Int,
        //val serviceID: Int,
        val driverID: Int,
        val shuttleID: Int,
        val routeID: Int?,
        val startTime: LocalTime?,
        val startDate: LocalDate?,
        val routeName: String?,
        val status: AssignmentStatus,
        val stops: List<Stop>
)