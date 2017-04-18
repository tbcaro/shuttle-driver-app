package com.polaris.app.driver.controller.adapter

import com.polaris.app.driver.service.bo.Assignment
import java.time.LocalTime


class AssignmentDetailsAdapter {
    var shuttleId: Int? = null
    var shuttleName: String = ""
    var driverId: Int? = null
    var driverName: String = ""
    var routeId: Int? = null
    var routeName: String = ""
    var startTime: LocalTime? = null
    var assignmentReport: AssignmentReport? = AssignmentReport()

    fun fromAssignment(assignment: Assignment) {
        // TODO : Add missing (commented out) fields
        this.driverId = assignment.driverID
        this.shuttleId = assignment.shuttleID
//                    this.driverName = assignment.driverName
//                    this.shuttleName = assignment.shuttleName
        this.routeId = assignment.routeID
        this.routeName = assignment.routeName ?: "Custom Route"
        this.startTime = assignment.startTime

        val report = AssignmentReport()
        report.assignmentId = assignment.assignmentID

        val stopAdapters = arrayListOf<AssignmentStopAdapter>()
        assignment.stops.forEach {
            val stopAdapter = AssignmentStopAdapter()

            stopAdapter.assingmentStopId = it.assignmentStopID
            stopAdapter.stopId = it.stopID
            stopAdapter.order = it.index
            stopAdapter.name = it.stopName
            stopAdapter.address = it.address
            stopAdapter.estArriveTime = it.ETA?.toLocalTime()
            stopAdapter.estDepartTime = it.ETD?.toLocalTime()
            stopAdapter.actualArriveTime = it.TOA?.toLocalTime()
            stopAdapter.actualDepartTime = it.TOD?.toLocalTime()

            stopAdapters.add(stopAdapter)
        }
        report.assignmentStops = stopAdapters
        this.assignmentReport = report
    }
}