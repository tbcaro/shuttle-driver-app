package com.polaris.app.driver.service.impl

import com.polaris.app.driver.repository.ActiveRepository
import com.polaris.app.driver.repository.entity.AssignmentEntity
import com.polaris.app.driver.repository.entity.AssignmentStopEntity
import com.polaris.app.driver.service.ActiveService
import com.polaris.app.driver.service.bo.Assignment
import com.polaris.app.driver.service.bo.Stop
import java.time.LocalDate

class ActiveServiceImpl(val activeRepository: ActiveRepository): ActiveService{
    override fun retrieveAssignment(driverID: Int, shuttleID: Int, startDate: LocalDate): Assignment {
        val a = this.activeRepository.findAssignment(driverID, shuttleID, startDate)
        val assignmentStops = arrayListOf<Stop>()
        val stopEntities = this.activeRepository.findAssignmentStops(a.assignmentID)

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
            assignmentStops.add(stop)
        }
        return Assignment(
                assignmentID = a.assignmentID,
                serviceID = a.serviceID,
                driverID = a.driverID,
                shuttleID = a.shuttleID,
                routeID = a.routeID,
                startTime = a.startTime,
                startDate = a.startDate,
                routeName = a.routeName,
                status = a.status,
                stops = assignmentStops
        )
    }

    override fun startRoute(shuttleID: Int, assignmentID: Int) {
        this.activeRepository.beginRoute(shuttleID, assignmentID)
    }

    override fun endService(shuttleID: Int) {
        this.activeRepository.endService(shuttleID)
    }
}