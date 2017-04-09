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
class SelectAssignmentController(private val authService: AuthenticationService) {

    @RequestMapping("/activate")
    fun activate(model: Model, http: HttpServletRequest, activateAdapter: ActivateAdapter) : String {
        if (authService.isAuthenticated(http)) {
            val userContext = authService.getUserContext(http)

            activateAdapter.selectedShuttleId?.let {
                println("Activating shuttle activity: ")
                println("shuttleId: ${it}")
                println("driverId: ${userContext.userId}")
                println("username: ${userContext.username}")
                println("servicecode: ${userContext.servicecode}")

                // TBC : TODO : Check to see if selected shuttle is already in use. If it is, throw error and do not allow activation.

                if (!authService.isShuttleActive(http)) {
                    // TBC : TODO : Create shuttleActivity with shuttleId
                    // TBC : Add shuttleId to session info
                    authService.addShuttleId(http, it)
                }

                // TBC : TODO : If successful, return app page
                // TBC : TODO : If failure, return menu with error
                return "redirect:/select-assignment"
            } ?: return "redirect:/menu"

        } else throw AuthenticationException("Error: user logged out")
    }

    @RequestMapping("/select-assignment")
    fun selectAssignment(model: Model, http: HttpServletRequest) : String {
        if (authService.isAuthenticated(http)) {
            if (authService.isShuttleActive(http)) {
                // TBC : TODO : Check to see if shuttle activity exists.
                    // TBC : TODO : If so, carry on
                    // TBC : TODO : If not, throw error and return to menu
                return "select-assignment"
            } else {
                return "redirect:/menu"
            }
        } else throw AuthenticationException("Error: user logged out")
    }

    @RequestMapping("/deactivate")
    fun deactivate(model: Model, http: HttpServletRequest) : String {
        if (authService.isAuthenticated(http)) {

            if (authService.isShuttleActive(http)) {
                // TBC : TODO : Delete shuttleActivity if one exists based on session info
                // TBC : Remove shuttleId from session info
                authService.deleteShuttleId(http)
            }

            return "redirect:/menu"
        } else throw AuthenticationException("Error: user logged out")
    }
}