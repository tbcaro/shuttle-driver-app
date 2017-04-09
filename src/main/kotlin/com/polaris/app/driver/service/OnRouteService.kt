package com.polaris.app.driver.service

import com.polaris.app.driver.service.bo.Index
import com.polaris.app.driver.service.bo.Stop
import java.time.LocalDateTime

interface OnRouteService{
    fun atStop(assignmentID: Int, TOA: LocalDateTime, index: Int)
    fun leaveStop(assignmentID: Int, TOD: LocalDateTime, index: Int): List<Stop>
    fun getIndexData(assignmentID: Int, index: Int): Index
    fun endRoute(assignmentID: Int, TOD: LocalDateTime, index: Int)
}