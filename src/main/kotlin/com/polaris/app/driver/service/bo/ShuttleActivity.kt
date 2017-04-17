package com.polaris.app.driver.service.bo

import com.polaris.app.dispatch.controller.adapter.enums.ShuttleState
import java.math.BigDecimal

data class ShuttleActivity(
        val shuttleID: Int,
        val driverID: Int,
        val assignmentID: Int,
        val assignmentStopID: Int,
        val index: Int,
        val latitude: BigDecimal,
        val longitude: BigDecimal,
        val heading: BigDecimal,
        val status: ShuttleState
)