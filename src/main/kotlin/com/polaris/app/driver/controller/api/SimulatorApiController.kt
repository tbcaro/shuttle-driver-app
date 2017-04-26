package com.polaris.app.driver.controller.api

import com.polaris.app.driver.controller.adapter.*
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
class SimulatorApiController(private val authService: AuthenticationService
) {

    @RequestMapping("/stayAlive")
    fun stayAlive(http: HttpServletRequest) : ResponseEntity<Any?> {
        return ResponseEntity(HttpStatus.OK)
    }

    @RequestMapping("/loadSimulation")
    fun loadSimulation(http: HttpServletRequest) : ResponseEntity<Any?> {
        return ResponseEntity(HttpStatus.OK)
    }

    @RequestMapping("/saveSimulation")
    fun saveSimulation(http: HttpServletRequest, @RequestBody saveSimAdapter: SaveSimAdapter) : ResponseEntity<Any?> {
        return ResponseEntity(HttpStatus.OK)
    }
}