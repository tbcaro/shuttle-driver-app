package com.polaris.app.driver.service

import com.polaris.app.dispatch.controller.adapter.enums.ShuttleState
import java.math.BigDecimal

interface UpdateService{
    fun updateShuttle(shuttleid: Int, assignmentid: Int, heading: BigDecimal, latitude: BigDecimal, longitude: BigDecimal, index: Int, status: ShuttleState)
}