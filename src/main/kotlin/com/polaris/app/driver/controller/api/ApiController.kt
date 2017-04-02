package com.polaris.app.driver.controller.api

import com.polaris.app.driver.controller.adapter.ShuttleActivityAdapter
import com.polaris.app.driver.service.AuthenticationService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/api")
class ApiController(private val authService: AuthenticationService) {

    @RequestMapping("/fetchAllAssignments")
    fun fetchAllAssignments(http: HttpServletRequest) : ResponseEntity<Int> {
        // TBC : TODO : Return all assignments for today
        return ResponseEntity(1, HttpStatus.OK)
    }

    @RequestMapping("/fetchActiveAssignment")
    fun fetchActiveAssignment(http: HttpServletRequest) : ResponseEntity<Int> {
        // TBC : TODO : Return active assignment based on activity in session data and then retrieved from DB
        return ResponseEntity(1, HttpStatus.OK)
    }

    @RequestMapping("/postActivity")
    fun postActivity(http: HttpServletRequest, @RequestBody shuttleActivityAdapter: ShuttleActivityAdapter) : ResponseEntity<Int> {
        // TBC : TODO : Return all assignments for today
        return ResponseEntity(1, HttpStatus.OK)
    }
}