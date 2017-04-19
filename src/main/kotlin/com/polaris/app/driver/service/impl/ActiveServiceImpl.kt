package com.polaris.app.driver.service.impl

import com.polaris.app.driver.repository.ActiveRepository
import com.polaris.app.driver.repository.entity.AssignmentEntity
import com.polaris.app.driver.repository.entity.AssignmentStopEntity
import com.polaris.app.driver.service.ActiveService
import com.polaris.app.driver.service.bo.Assignment
import com.polaris.app.driver.service.bo.ShuttleActivity
import com.polaris.app.driver.service.bo.Stop
import java.time.LocalDate

class ActiveServiceImpl(val activeRepository: ActiveRepository): ActiveService{
    override fun retrieveAssignment(assignmentID: Int): Assignment {
        val a = this.activeRepository.findAssignment(assignmentID)
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
                    stopName = it.stopName,
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

    override fun retrieveAssignments(driverID: Int, shuttleID: Int, startDate: LocalDate): List<Assignment> {
        val a = this.activeRepository.findAssignments(driverID, shuttleID, startDate)
        val assignments = arrayListOf<Assignment>()

        a.forEach{
            val stopEntities = this.activeRepository.findAssignmentStops(it.assignmentID)
            val assignmentStops = arrayListOf<Stop>()

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
                        stopName = it.stopName,
                        address = it.address,
                        latitude = it.latitude,
                        longitude = it.longitude
                )
                assignmentStops.add(stop)
            }
            val assignment = Assignment(
                    serviceID = it.serviceID,
                    assignmentID = it.assignmentID,
                    startDate = it.startDate,
                    startTime = it.startTime,
                    routeID = it.routeID,
                    routeName = it.routeName ?: "",
                    driverID = it.driverID,
                    shuttleID = it.shuttleID,
                    status = it.status,
                    stops = assignmentStops
            )
            assignments.add(assignment)
        }
        return assignments
    }

    override fun retrieveShuttleActivity(shuttleID: Int): ShuttleActivity {
        val sae = this.activeRepository.findShuttleActivity(shuttleID)
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