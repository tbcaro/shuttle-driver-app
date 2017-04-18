package com.polaris.app.driver.service

import com.polaris.app.driver.service.bo.Assignment
import com.polaris.app.driver.service.bo.ShuttleActivity
import java.time.LocalDate

interface ActiveService{
    fun retrieveAssignment(assignmentID: Int): Assignment
    fun startRoute(shuttleID: Int, assignmentID: Int)
    fun endService(shuttleID: Int)
    fun retrieveAssignments(driverID: Int, shuttleID: Int, startDate: LocalDate): List<Assignment>
    fun retrieveShuttleActivity(shuttleID: Int): ShuttleActivity
}