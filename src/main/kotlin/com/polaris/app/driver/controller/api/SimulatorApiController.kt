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
class SimulatorApiController(private val authService: AuthenticationService, private val simulationService: SimulationService
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
}