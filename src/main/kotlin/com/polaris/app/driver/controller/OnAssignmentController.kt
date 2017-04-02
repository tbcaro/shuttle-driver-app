package com.polaris.app.driver.controller

import com.polaris.app.driver.controller.adapter.ActivateAdapter
import com.polaris.app.driver.controller.exception.AuthenticationException
import com.polaris.app.driver.service.AuthenticationService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import javax.servlet.http.HttpServletRequest

@Controller
class OnAssignmentController(private val authService: AuthenticationService) {

    @RequestMapping("/begin-assignment")
    fun beginAssignment(model: Model, http: HttpServletRequest) : String {
        if (authService.isAuthenticated(http)) {
            if (authService.isShuttleActive(http)) {
                // TBC : TODO : Check to see if shuttle activity exists.
                // TBC : TODO : If so, add assignmentId to the activity and begin tracking current index
                // TBC : TODO : If not, throw error and return to menu
                return "on-assignment"
            } else {
                return "redirect:/menu"
            }
        } else throw AuthenticationException("Error: user logged out")
    }

    @RequestMapping("/on-assignment")
    fun onAssignment(model: Model, http: HttpServletRequest) : String {
        if (authService.isAuthenticated(http)) {
            if (authService.isShuttleActive(http)) {
                // TBC : TODO : Check to see if shuttle activity exists.
                    // TBC : TODO : If so, carry on
                    // TBC : TODO : If not, throw error and return to menu
                return "on-assignment"
            } else {
                return "redirect:/menu"
            }
        } else throw AuthenticationException("Error: user logged out")
    }

    @RequestMapping("/end-assignment")
    fun endAssignment(model: Model, http: HttpServletRequest) : String {
        if (authService.isAuthenticated(http)) {
            if (authService.isShuttleActive(http)) {
                // TBC : TODO : Check to see if shuttle activity exists.
                // TBC : TODO : If so, delete assignment id from activity, but do NOT mark assignment COMPLETED
                // TBC : TODO : If not, throw error and return to menu
                return "select-assignment"
            } else {
                return "redirect:/menu"
            }
        } else throw AuthenticationException("Error: user logged out")
    }

    @RequestMapping("/finish-assignment")
    fun finishAssignment(model: Model, http: HttpServletRequest) : String {
        if (authService.isAuthenticated(http)) {
            if (authService.isShuttleActive(http)) {
                // TBC : TODO : Check to see if shuttle activity exists.
                // TBC : TODO : If so, delete assignment id from activity AND mark assignment COMPLETED
                // TBC : TODO : If not, throw error and return to menu
                return "select-assignment"
            } else {
                return "redirect:/menu"
            }
        } else throw AuthenticationException("Error: user logged out")
    }
}