package com.polaris.app.driver.service.impl

import com.polaris.app.dispatch.controller.adapter.enums.ShuttleState
import com.polaris.app.driver.repository.UpdateRepository
import com.polaris.app.driver.service.UpdateService
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
}