package com.polaris.app.driver.repository

import com.polaris.app.driver.repository.entity.AssignmentEntity
import com.polaris.app.driver.repository.entity.AssignmentStopEntity
import com.polaris.app.driver.repository.entity.ShuttleActivityEntity
import java.time.LocalDate

interface ActiveRepository{
    fun findAssignment(assignmentID: Int): AssignmentEntity
    fun findAssignmentStops(assignmentID: Int): List<AssignmentStopEntity>
    fun beginRoute(shuttleID: Int, assignmentID: Int)
    fun endService(shuttleID: Int)
    fun findAssignments(driverID: Int, shuttleID: Int, startDate: LocalDate): List<AssignmentEntity>
    fun findShuttleActivity(serviceID: Int): ShuttleActivityEntity
}