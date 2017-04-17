package com.polaris.app.driver.service.impl

import com.polaris.app.driver.repository.OnRouteRepository
import com.polaris.app.driver.repository.entity.AssignmentStopEntity
import com.polaris.app.driver.service.OnRouteService
import com.polaris.app.driver.service.bo.Index
import com.polaris.app.driver.service.bo.ShuttleActivity
import com.polaris.app.driver.service.bo.Stop
import java.time.LocalDateTime

class OnRouteServiceImpl(val onRouteRepository: OnRouteRepository): OnRouteService{
    /*override fun atStop(assignmentID: Int, TOA: LocalDateTime, index: Int) {
        this.onRouteRepository.stop(assignmentID, TOA, index)
    }

    override fun leaveStop(assignmentID: Int, TOD: LocalDateTime, index: Int): List<Stop> {
        this.onRouteRepository.driveUpdate(assignmentID,TOD, index)
        val stops = arrayListOf<Stop>()
        val stopEntities = this.onRouteRepository.updateStopData(assignmentID, index)

        stopEntities.forEach{
            val stop = Stop(
                    assignmentStopID = it.assignmentStopID,
                    assignmentID = it.assignmentID,
                    index = it.index,
                    ETA = it.ETA,
                    ETD = it.ETD,
                    TOA = it.TOA,
                    TOD = it.TOD,
                    stopID = it.stopID,
                    address = it.address,
                    latitude = it.latitude,
                    longitude = it.longitude
            )
            stops.add(stop)
        }
        return stops
    }

    override fun getIndexData(assignmentID: Int, index: Int): Index {
        val stopCheckEntity = this.onRouteRepository.stopCheck(assignmentID, index)

        return Index(
                index = stopCheckEntity.index,
                remainingStops = stopCheckEntity.remainingStops
        )
    }*/

    override fun endAssignment(assignmentID: Int, early: Boolean) {
        if (early){
            this.onRouteRepository.earlyEndAssignment(assignmentID)
        }
        else {
            if (this.onRouteRepository.checkAssignmentStatus(assignmentID)) {
                this.onRouteRepository.endAssignmentWithTime(assignmentID)
            }
            this.onRouteRepository.endAssignment(assignmentID)
        }
    }

    override fun retrieveShuttleActivity(shuttleID: Int): ShuttleActivity {
        val sae = this.onRouteRepository.findShuttleActivity(shuttleID)
        val sa = ShuttleActivity(
                shuttleID = sae.shuttleID,
                driverID = sae.driverID,
                assignmentID = sae.assignmentID,
                assignmentStopID = sae.assignmentStopID,
                index = sae.index,
                latitude = sae.latitude,
                longitude = sae.longitude,
                heading = sae.heading,
                status = sae.status
        )
        return sa
    }
}