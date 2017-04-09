package com.polaris.app.driver.service.impl

import com.polaris.app.driver.repository.InactiveRepository
import com.polaris.app.driver.service.InactiveService
import com.polaris.app.driver.service.bo.ActiveShuttle
import com.polaris.app.driver.service.bo.InactiveShuttle
import java.math.BigDecimal

class InactiveServiceImpl(val inactiveRepository: InactiveRepository): InactiveService{
    override fun retrieveShuttles(serviceid: Int): List<InactiveShuttle> {
        val shuttles = arrayListOf<InactiveShuttle>()
        val shuttleEntities = this.inactiveRepository.findShuttles(serviceid)

        shuttleEntities.forEach{
            val shuttle = InactiveShuttle(
                    shuttleID = it.shuttleid,
                    shuttleName = it.shuttleName
            )
            shuttles.add(shuttle)
        }
        return shuttles
    }

    override fun beginActiveService(s: ActiveShuttle) {
        this.inactiveRepository.beginService(s)
    }

    override fun beginActiveService(shuttleID: Int, driverID: Int, latitude: BigDecimal, longitude: BigDecimal, status: String, heading: BigDecimal) {
        val s = ActiveShuttle(
                shuttleID = shuttleID,
                driverID = driverID,
                latitude = latitude,
                longitude = longitude,
                status = status,
                heading = heading
        )

        if (this.inactiveRepository.checkShuttle(shuttleID)) {
            if (this.inactiveRepository.checkDriver(driverID)) {
                this.inactiveRepository.beginService(s)
            }
            else{

            }

        }
        else{

        }
    }
}