package com.polaris.app.driver.repository

import com.polaris.app.driver.controller.adapter.enums.AssignmentStatus
import com.polaris.app.driver.repository.entity.AssignmentStopEntity
import com.polaris.app.driver.repository.entity.StopCheckEntity
import java.time.LocalDateTime

interface OnRouteRepository{
    /*fun stop(assignmentid: Int, TOA: LocalDateTime, index: Int)
    fun driveUpdate(assignmentid: Int, TOD: LocalDateTime, index: Int)
    fun stopCheck(assignmentid: Int, index: Int): StopCheckEntity
    fun updateStopData(assignmentid: Int, index: Int): List<AssignmentStopEntity>*/
    fun endAssignment(assignmentid: Int)
    fun earlyEndAssignment(assignmentid: Int)
    fun checkAssignmentStatus(assignmentid: Int): Boolean
    fun endAssignmentWithTime(assignmentid: Int)
}