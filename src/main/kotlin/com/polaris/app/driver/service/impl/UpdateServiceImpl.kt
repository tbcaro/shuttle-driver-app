package com.polaris.app.driver.service.impl

import com.polaris.app.dispatch.controller.adapter.enums.ShuttleState
import com.polaris.app.driver.repository.UpdateRepository
import com.polaris.app.driver.service.UpdateService
import com.polaris.app.driver.service.bo.ShuttleActivity
import com.polaris.app.driver.service.bo.UpdateShuttle
import java.math.BigDecimal

class UpdateServiceImpl(val updateRepository: UpdateRepository): UpdateService{
    override fun updateShuttle(shuttleid: Int, assignmentid: Int, heading: BigDecimal, latitude: BigDecimal, longitude: BigDecimal, index: Int, status: ShuttleState) {
        val shuttle = UpdateShuttle(
                shuttleID = shuttleid,
                assignmentID = assignmentid,
                heading = heading,
                latitude = latitude,
                longitude = longitude,
                index = index,
                status = status
        )
        val u = this.updateRepository.checkPreviousState(shuttle)
        this.updateRepository.pushShuttleActivity(shuttle)
        this.updateRepository.pushStopData(shuttle, u)
    }

    override fun retrieveShuttleActivity(shuttleID: Int): ShuttleActivity {
        val sae = this.updateRepository.findShuttleActivity(shuttleID)
        val sa = ShuttleActivity(
                shuttleID = sae.shuttleID,
                driverID = sae.driverID,
                assignmentID = sae.assignmentID,
                index = sae.index,
                latitude = sae.latitude,
                longitude = sae.longitude,
                heading = sae.heading,
                status = sae.status
        )
        return sa
    }
}