package com.polaris.app.driver.service

import com.polaris.app.driver.service.bo.Assignment
import java.time.LocalDate

interface ActiveService{
    fun retrieveAssignment(driverID: Int, shuttleID: Int, startDate: LocalDate): Assignment
    fun startRoute(shuttleID: Int, assignmentID: Int)
    fun endService(shuttleID: Int)
}