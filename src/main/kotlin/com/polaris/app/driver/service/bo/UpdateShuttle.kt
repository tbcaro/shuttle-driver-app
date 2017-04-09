package com.polaris.app.driver.service.bo

import com.polaris.app.dispatch.controller.adapter.enums.ShuttleState
import java.math.BigDecimal

data class UpdateShuttle(
        val shuttleID: Int,
        val assignmentID: Int,
        val latitude: BigDecimal,
        val longitude: BigDecimal,
        val heading: BigDecimal,
        val status: ShuttleState,
        val index: Int
)