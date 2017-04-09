package com.polaris.app.driver.service.bo

import java.math.BigDecimal

data class UpdateShuttle(
        val shuttleID: Int,
        val latitude: BigDecimal,
        val longitude: BigDecimal,
        val heading: BigDecimal
)