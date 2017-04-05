package com.polaris.app.driver.controller.api

import com.polaris.app.driver.controller.adapter.AssignmentDetailsAdapter
import com.polaris.app.driver.controller.adapter.AssignmentReport
import com.polaris.app.driver.controller.adapter.AssignmentStopAdapter
import com.polaris.app.driver.controller.adapter.ShuttleActivityAdapter
import com.polaris.app.driver.controller.exception.AuthenticationException
import com.polaris.app.driver.service.AuthenticationService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalTime
import java.util.*
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/api")
class ApiController(private val authService: AuthenticationService) {

    @RequestMapping("/fetchAllAssignments")
    fun fetchAllAssignments(http: HttpServletRequest) : ResponseEntity<List<AssignmentDetailsAdapter>?> {
        if (authService.isAuthenticated(http)) {
            val userContext = authService.getUserContext(http)

            if (authService.isShuttleActive(http)) {
                /*
                    TBC :
                    TODO : Query for all assignments assigned to driver, shuttle combo for today
                 */
                val shuttleId = userContext.shuttleId
                val driverId = userContext.userId

                val stop1 = AssignmentStopAdapter()
                stop1.stopId = 1
                stop1.name = "Test Stop 1"
                stop1.order = 1
                stop1.assingmentStopId = 1
                stop1.address = "1 Example Address Rd"
                stop1.estArriveTime = LocalTime.of(17, 0)
                stop1.estDepartTime = LocalTime.of(17, 30)

                val stop2 = AssignmentStopAdapter()
                stop2.stopId = 2
                stop2.name = "Test Stop 2"
                stop2.order = 2
                stop2.assingmentStopId = 2
                stop2.address = "2 Example Address Rd"
                stop2.estArriveTime = LocalTime.of(18, 0)
                stop2.estDepartTime = LocalTime.of(18, 30)

                val stop3 = AssignmentStopAdapter()
                stop3.stopId = 3
                stop3.name = "Test Stop 3"
                stop3.order = 3
                stop3.assingmentStopId = 3
                stop3.address = "3 Example Address Rd"
                stop3.estArriveTime = LocalTime.of(19, 0)
                stop3.estDepartTime = LocalTime.of(19, 30)

                val assignment1 = AssignmentDetailsAdapter()
                assignment1.driverId = driverId
                assignment1.shuttleId = shuttleId
                assignment1.driverName = "Test Driver"
                assignment1.shuttleName = "Test Shuttle"
                assignment1.routeId = 1
                assignment1.routeName = "Test Route 1"
                assignment1.startTime = LocalTime.now()
                assignment1.assignmentReport = AssignmentReport()
                assignment1.assignmentReport?.assignmentId = 1
                assignment1.assignmentReport?.assignmentStops = arrayListOf(stop1, stop2, stop3)

                val assignment2 = AssignmentDetailsAdapter()
                assignment2.driverId = driverId
                assignment2.shuttleId = shuttleId
                assignment2.driverName = "Test Driver"
                assignment2.shuttleName = "Test Shuttle"
                assignment2.routeId = 2
                assignment2.routeName = "Test Route 2"
                assignment2.startTime = LocalTime.now()
                assignment2.assignmentReport = AssignmentReport()
                assignment2.assignmentReport?.assignmentId = 2
                assignment2.assignmentReport?.assignmentStops = arrayListOf(stop3, stop2, stop1)

                val assignment3 = AssignmentDetailsAdapter()
                assignment3.driverId = driverId
                assignment3.shuttleId = shuttleId
                assignment3.driverName = "Test Driver"
                assignment3.shuttleName = "Test Shuttle"
                assignment3.routeId = 3
                assignment3.routeName = "Test Route 3"
                assignment3.startTime = LocalTime.now()
                assignment3.assignmentReport = AssignmentReport()
                assignment3.assignmentReport?.assignmentId = 3
                assignment3.assignmentReport?.assignmentStops = arrayListOf(stop2, stop1, stop3)

                val assignmentAdapters = arrayListOf(assignment1, assignment2, assignment3)

                return ResponseEntity(assignmentAdapters, HttpStatus.OK)
            } else {
                return ResponseEntity(null, HttpStatus.UNAUTHORIZED)
            }
        } else {
            return ResponseEntity(null, HttpStatus.UNAUTHORIZED)
        }
    }

    @RequestMapping("/fetchActiveAssignment")
    fun fetchActiveAssignment(http: HttpServletRequest) : ResponseEntity<AssignmentDetailsAdapter> {
        if (authService.isAuthenticated(http)) {
            val userContext = authService.getUserContext(http)

            if (authService.isShuttleActive(http)) {
                /*
                    TBC :
                    TODO : Query for assignment associated with activity
                 */
                val shuttleId = userContext.shuttleId
                val driverId = userContext.userId

                val stop1 = AssignmentStopAdapter()
                stop1.stopId = 1
                stop1.name = "Test Stop 1"
                stop1.order = 1
                stop1.assingmentStopId = 1
                stop1.address = "1 Example Address Rd"
                stop1.estArriveTime = LocalTime.of(17, 0)
                stop1.estDepartTime = LocalTime.of(17, 30)

                val stop2 = AssignmentStopAdapter()
                stop2.stopId = 2
                stop2.name = "Test Stop 2"
                stop2.order = 2
                stop2.assingmentStopId = 2
                stop2.address = "2 Example Address Rd"
                stop2.estArriveTime = LocalTime.of(18, 0)
                stop2.estDepartTime = LocalTime.of(18, 30)

                val stop3 = AssignmentStopAdapter()
                stop3.stopId = 3
                stop3.name = "Test Stop 3"
                stop3.order = 3
                stop3.assingmentStopId = 3
                stop3.address = "3 Example Address Rd"
                stop3.estArriveTime = LocalTime.of(19, 0)
                stop3.estDepartTime = LocalTime.of(19, 30)

                val assignment1 = AssignmentDetailsAdapter()
                assignment1.driverId = driverId
                assignment1.shuttleId = shuttleId
                assignment1.driverName = "Test Driver"
                assignment1.shuttleName = "Test Shuttle"
                assignment1.routeId = 1
                assignment1.routeName = "Test Route 1"
                assignment1.startTime = LocalTime.now()
                assignment1.assignmentReport = AssignmentReport()
                assignment1.assignmentReport?.assignmentId = 1
                assignment1.assignmentReport?.assignmentStops = arrayListOf(stop1, stop2, stop3)

                return ResponseEntity(assignment1, HttpStatus.OK)
            } else {
                return ResponseEntity(null, HttpStatus.UNAUTHORIZED)
            }
        } else {
            return ResponseEntity(null, HttpStatus.UNAUTHORIZED)
        }
    }

    @RequestMapping("/postActivity")
    fun postActivity(http: HttpServletRequest, @RequestBody shuttleActivityAdapter: ShuttleActivityAdapter) : ResponseEntity<AssignmentDetailsAdapter?> {
        if (authService.isAuthenticated(http)) {
            val userContext = authService.getUserContext(http)

            if (authService.isShuttleActive(http)) {
                shuttleActivityAdapter.shuttleId = userContext.shuttleId
                shuttleActivityAdapter.driverId = userContext.userId

                // TBC : TODO : Return the assignment as the response from this. On client check if different from client version.
                val stop1 = AssignmentStopAdapter()
                stop1.stopId = 1
                stop1.name = "Test Stop 1"
                stop1.order = 1
                stop1.assingmentStopId = 1
                stop1.address = "1 Example Address Rd"
                stop1.estArriveTime = LocalTime.of(17, 0)
                stop1.estDepartTime = LocalTime.of(17, 30)

                val stop2 = AssignmentStopAdapter()
                stop2.stopId = 2
                stop2.name = "Test Stop 2"
                stop2.order = 2
                stop2.assingmentStopId = 2
                stop2.address = "2 Example Address Rd"
                stop2.estArriveTime = LocalTime.of(18, 0)
                stop2.estDepartTime = LocalTime.of(18, 30)

                val stop3 = AssignmentStopAdapter()
                stop3.stopId = 3
                stop3.name = "Test Stop 3"
                stop3.order = 3
                stop3.assingmentStopId = 3
                stop3.address = "3 Example Address Rd"
                stop3.estArriveTime = LocalTime.of(19, 0)
                stop3.estDepartTime = LocalTime.of(19, 30)

                val assignment1 = AssignmentDetailsAdapter()
                assignment1.driverId = userContext.userId
                assignment1.shuttleId = userContext.shuttleId
                assignment1.driverName = "Test Driver"
                assignment1.shuttleName = "Test Shuttle"
                assignment1.routeId = 1
                assignment1.routeName = "Test Route 1"
                assignment1.startTime = LocalTime.now()
                assignment1.assignmentReport = AssignmentReport()
                assignment1.assignmentReport?.assignmentId = 1
                assignment1.assignmentReport?.assignmentStops = arrayListOf(stop1, stop2, stop3)
                return ResponseEntity(assignment1, HttpStatus.OK)
            } else {
                return ResponseEntity(null, HttpStatus.UNAUTHORIZED)
            }
        } else {
            return ResponseEntity(null, HttpStatus.UNAUTHORIZED)
        }
    }
}