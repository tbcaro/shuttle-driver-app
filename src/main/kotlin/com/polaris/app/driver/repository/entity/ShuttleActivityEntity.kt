package com.polaris.app.driver.repository.entity

import com.polaris.app.dispatch.controller.adapter.enums.ShuttleState
import java.math.BigDecimal

data class ShuttleActivityEntity(
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