package com.polaris.app.driver.controller.api

import com.polaris.app.driver.controller.adapter.AssignmentDetailsAdapter
import com.polaris.app.driver.controller.adapter.AssignmentReport
import com.polaris.app.driver.controller.adapter.AssignmentStopAdapter
import com.polaris.app.driver.controller.adapter.ShuttleActivityAdapter
import com.polaris.app.driver.controller.exception.AuthenticationException
import com.polaris.app.driver.service.ActiveService
import com.polaris.app.driver.service.AuthenticationService
import com.polaris.app.driver.service.OnRouteService
import com.polaris.app.driver.service.UpdateService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate
import java.time.LocalTime
import java.util.*
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/api")
class ApiController(private val authService: AuthenticationService,
                    private val activeService: ActiveService,
                    private val onRouteService: OnRouteService,
                    private val updateService: UpdateService
) {

    @RequestMapping("/fetchAllAssignments")
    fun fetchAllAssignments(http: HttpServletRequest) : ResponseEntity<List<AssignmentDetailsAdapter>?> {
        if (authService.isAuthenticated(http)) {
            val userContext = authService.getUserContext(http)

            if (authService.isShuttleActive(http)) {
                val assignments = activeService.retrieveAssignments(userContext.userId, userContext.shuttleId, LocalDate.now())
                val assignmentAdapters = arrayListOf<AssignmentDetailsAdapter>()

                assignments.forEach {
                    val assignment = AssignmentDetailsAdapter()

                    assignment.fromAssignment(it)

                    assignmentAdapters.add(assignment)
                }

                return ResponseEntity(assignmentAdapters, HttpStatus.OK)
            } else {
                return ResponseEntity(null, HttpStatus.UNAUTHORIZED)
            }
        } else {
            return ResponseEntity(null, HttpStatus.UNAUTHORIZED)
        }
    }

    @RequestMapping("/fetchActiveAssignment")
    fun fetchActiveAssignment(http: HttpServletRequest) : ResponseEntity<AssignmentDetailsAdapter?> {
        if (authService.isAuthenticated(http)) {
            val userContext = authService.getUserContext(http)

            if (authService.isShuttleActive(http)) {
                val activity = onRouteService.retrieveShuttleActivity(userContext.shuttleId)
                if (activity.assignmentID == null) {
                    return ResponseEntity(null, HttpStatus.OK)
                } else {
                    val assignment = activeService.retrieveAssignment(activity.assignmentID)
                    val assignmentDetails = AssignmentDetailsAdapter()

                    assignmentDetails.fromAssignment(assignment)

                    return ResponseEntity(assignmentDetails, HttpStatus.OK)
                }

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

                updateService.updateShuttle(
                        shuttleActivityAdapter.shuttleId,
                        shuttleActivityAdapter.assignmentId,
                        shuttleActivityAdapter.heading,
                        shuttleActivityAdapter.latitude,
                        shuttleActivityAdapter.longitude,
                        shuttleActivityAdapter.currentStopIndex,
                        shuttleActivityAdapter.status
                        )

                val activity = onRouteService.retrieveShuttleActivity(userContext.shuttleId)
                var assignmentDetails: AssignmentDetailsAdapter? = null

                if (activity.assignmentID != 0) {
                    val assignment = activeService.retrieveAssignment(activity.assignmentID)

                    assignmentDetails = AssignmentDetailsAdapter()
                    assignmentDetails.fromAssignment(assignment)
                }

                return ResponseEntity(assignmentDetails, HttpStatus.OK)
            } else {
                return ResponseEntity(null, HttpStatus.UNAUTHORIZED)
            }
        } else {
            return ResponseEntity(null, HttpStatus.UNAUTHORIZED)
        }
    }
}