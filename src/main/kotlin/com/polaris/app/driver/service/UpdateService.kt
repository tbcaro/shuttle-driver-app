package com.polaris.app.driver.service

import com.polaris.app.dispatch.controller.adapter.enums.ShuttleState
import com.polaris.app.driver.service.bo.ShuttleActivity
import java.math.BigDecimal

interface UpdateService{
    fun updateShuttle(shuttleid: Int, assignmentid: Int, heading: BigDecimal, latitude: BigDecimal, longitude: BigDecimal, index: Int, status: ShuttleState)
    fun retrieveShuttleActivity(shuttleID: Int): ShuttleActivity
}