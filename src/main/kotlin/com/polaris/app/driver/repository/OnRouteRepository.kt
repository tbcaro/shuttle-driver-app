package com.polaris.app.driver.repository

import com.polaris.app.driver.repository.entity.AssignmentStopEntity
import com.polaris.app.driver.repository.entity.StopCheckEntity
import java.time.LocalDateTime

interface OnRouteRepository{
    fun stop(assignmentid: Int, TOA: LocalDateTime, index: Int)
    fun driveUpdate(assignmentid: Int, TOD: LocalDateTime, index: Int)
    fun stopCheck(assignmentid: Int, index: Int): StopCheckEntity
    fun updateStopData(assignmentid: Int, index: Int): List<AssignmentStopEntity>
    fun endRoute(assignmentid: Int)
}