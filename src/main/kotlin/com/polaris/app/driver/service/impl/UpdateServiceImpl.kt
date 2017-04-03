package com.polaris.app.driver.service.impl

import com.polaris.app.driver.repository.UpdateRepository
import com.polaris.app.driver.service.UpdateService
import com.polaris.app.driver.service.bo.UpdateShuttle
import java.math.BigDecimal

class UpdateServiceImpl(val updateRepository: UpdateRepository): UpdateService{
    override fun updateShuttle(shuttleid: Int, heading: BigDecimal, latitude: BigDecimal, longitude: BigDecimal) {
        val shuttle = UpdateShuttle(
                shuttleID = shuttleid,
                heading = heading,
                latitude = latitude,
                longitude = longitude
        )
        this.updateRepository.pushShuttleActivity(shuttle)
    }
}