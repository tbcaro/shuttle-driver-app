package com.polaris.app.driver.controller.api

import com.polaris.app.driver.controller.adapter.*
import com.polaris.app.driver.controller.exception.AuthenticationException
import com.polaris.app.driver.service.*
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
class SimulatorApiController(private val authService: AuthenticationService,
                             private val simulationService: SimulationService,
                             private val updateService: UpdateService,
                             private val onRouteService: OnRouteService,
                             private val inactiveService: InactiveService,
                             private val activeService: ActiveService
) {

    @RequestMapping("/stayAlive")
    fun stayAlive(http: HttpServletRequest) : ResponseEntity<Any?> {
        return ResponseEntity(HttpStatus.OK)
    }

    @RequestMapping("/loadSimulation")
    fun loadSimulation(http: HttpServletRequest, shuttleId: Int) : ResponseEntity<Any?> {
        try {
            val simCycles = simulationService.loadSimulation(shuttleId)
            return ResponseEntity(simCycles, HttpStatus.OK)
        } catch (ex: Exception) {
            return ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @RequestMapping("/saveSimulation")
    fun saveSimulation(http: HttpServletRequest, @RequestBody saveSimAdapter: SaveSimAdapter) : ResponseEntity<Any?> {
        try {
            val userContext = authService.getUserContext(http)
            simulationService.saveSimulation(saveSimAdapter.toSimCycleList(userContext.userId))
            return ResponseEntity(HttpStatus.OK)
        } catch (ex: Exception) {
            return ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @RequestMapping("/postSimulation")
    fun postActivity(http: HttpServletRequest, @RequestBody shuttleActivityAdapter: ShuttleActivityAdapter) : ResponseEntity<Any?> {
        if (authService.isAuthenticated(http)) {
            val userContext = authService.getUserContext(http)
            var activityExists = false

            shuttleActivityAdapter.driverId = userContext.userId

            try {
                val activity = onRouteService.retrieveShuttleActivity(shuttleActivityAdapter.shuttleId)
                activityExists = true
            } catch (ex: Exception) { }

            if (!activityExists)
                inactiveService.beginActiveService(shuttleActivityAdapter.shuttleId!!, userContext.userId)

            updateService.updateShuttle(
                    shuttleActivityAdapter.shuttleId,
                    shuttleActivityAdapter.assignmentId,
                    shuttleActivityAdapter.heading,
                    shuttleActivityAdapter.latitude,
                    shuttleActivityAdapter.longitude,
                    shuttleActivityAdapter.currentStopIndex,
                    shuttleActivityAdapter.status
            )

            return ResponseEntity(HttpStatus.OK)
        } else {
            return ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @RequestMapping("/stopSimulation")
    fun postActivity(http: HttpServletRequest, shuttleId: Int) : ResponseEntity<Any?> {
        try {
            activeService.endService(shuttleId)
            return ResponseEntity(HttpStatus.OK)
        } catch (ex: Exception) {
            return ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
}