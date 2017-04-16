package com.polaris.app.driver.controller

import com.polaris.app.dispatch.controller.adapter.enums.ShuttleState
import com.polaris.app.driver.controller.adapter.ActivateAdapter
import com.polaris.app.driver.controller.exception.AuthenticationException
import com.polaris.app.driver.service.ActiveService
import com.polaris.app.driver.service.AuthenticationService
import com.polaris.app.driver.service.InactiveService
import com.polaris.app.driver.service.bo.ActiveShuttle
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import javax.servlet.http.HttpServletRequest

@Controller
class SelectAssignmentController(private val authService: AuthenticationService,
                                 private val inactiveService: InactiveService,
                                 private val activeService: ActiveService) {

    @RequestMapping("/activate")
    fun activate(attributes: RedirectAttributes, model: Model, http: HttpServletRequest, activateAdapter: ActivateAdapter) : String {
        if (authService.isAuthenticated(http)) {
            val userContext = authService.getUserContext(http)

            if (activateAdapter.selectedShuttleId == null) {
                attributes.addFlashAttribute("error", "Invalid shuttle ID")
                return "redirect:/menu"
            } else {
                // TBC : TODO : Check to see if selected shuttle is already in use. If it is, throw error and do not allow activation.

                if (!authService.isShuttleActive(http)) {
                    try {
                        inactiveService.beginActiveService(activateAdapter.selectedShuttleId!!, userContext.userId)

                        // TBC : Add shuttleId to session info
                        authService.addShuttleId(http, activateAdapter.selectedShuttleId!!)
                    } catch (ex: Exception) {
                        attributes.addFlashAttribute("error", ex.message)
                        return "redirect:/menu"
                    }
                }
            }

            return "redirect:/select-assignment"

        } else throw AuthenticationException("Error: user logged out")
    }

    @RequestMapping("/select-assignment")
    fun selectAssignment(attributes: RedirectAttributes, model: Model, http: HttpServletRequest) : String {
        if (authService.isAuthenticated(http)) {
            if (authService.isShuttleActive(http)) {
                // TBC : TODO : Check to see if shuttle activity exists.
                    // TBC : TODO : If so, carry on
                    // TBC : TODO : If not, throw error and return to menu
                return "select-assignment"
            } else {
                attributes.addFlashAttribute("error", "You are not currently active")
                return "redirect:/menu"
            }
        } else throw AuthenticationException("Error: user logged out")
    }

    @RequestMapping("/deactivate")
    fun deactivate(model: Model, http: HttpServletRequest) : String {
        if (authService.isAuthenticated(http)) {

            if (authService.isShuttleActive(http)) {
                // TBC : Delete shuttleActivity if one exists based on session info
                val userContext = authService.getUserContext(http)
                activeService.endService(userContext.shuttleId)
                // TBC : Remove shuttleId from session info
                authService.deleteShuttleId(http)
            }

            return "redirect:/menu"
        } else throw AuthenticationException("Error: user logged out")
    }
}