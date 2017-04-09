package com.polaris.app.driver.service

import java.math.BigDecimal

interface UpdateService{
    fun updateShuttle(shuttleid: Int, heading: BigDecimal, latitude: BigDecimal, longitude: BigDecimal)
}