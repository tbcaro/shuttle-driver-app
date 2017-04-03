package com.polaris.app.driver.service.bo

import java.math.BigDecimal

data class ActiveShuttle(
        val shuttleID: Int,
        val driverID: Int,
        val latitude: BigDecimal,
        val longitude: BigDecimal,
        val status: String,
        val heading: BigDecimal
)