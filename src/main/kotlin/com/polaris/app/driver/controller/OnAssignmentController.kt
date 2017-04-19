package com.polaris.app.driver.controller

import com.polaris.app.driver.controller.adapter.ActivateAdapter
import com.polaris.app.driver.controller.exception.AuthenticationException
import com.polaris.app.driver.service.ActiveService
import com.polaris.app.driver.service.AuthenticationService
import com.polaris.app.driver.service.OnRouteService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import javax.servlet.http.HttpServletRequest

@Controller
class OnAssignmentController(private val authService: AuthenticationService,
                             private val activeService: ActiveService,
                             private val onRouteService: OnRouteService
) {

    @RequestMapping("/begin-assignment")
    fun beginAssignment(attributes: RedirectAttributes, model: Model, http: HttpServletRequest, assignmentId: Int) : String {
        if (authService.isAuthenticated(http)) {
            if (authService.isShuttleActive(http)) {
                val userContext = authService.getUserContext(http)

                // TBC : Check to see if shuttle activity exists.
                // TBC : If so, add assignmentId to the activity, set assignment as IN_PROGRESS and track current stop index
                // TBC : If not, throw error and return to menu
                try {
                    val activity = activeService.retrieveShuttleActivity(userContext.shuttleId)
                    activeService.startRoute(userContext.shuttleId, assignmentId)
                } catch (ex: Exception) {
                    attributes.addFlashAttribute("error", "Activity does not exist")
                    return "redirect:/menu"
                }

                return "redirect:/on-assignment"
            } else {
                return "redirect:/menu"
            }
        } else throw AuthenticationException()
    }

    @RequestMapping("/on-assignment")
    fun onAssignment(attributes: RedirectAttributes, model: Model, http: HttpServletRequest) : String {
        if (authService.isAuthenticated(http)) {
            if (authService.isShuttleActive(http)) {
                val userContext = authService.getUserContext(http)
                // TBC : Check to see if shuttle activity exists.
                // TBC : If so, carry on
                // TBC : If not, throw error and return to menu
                try {
                    val activity = activeService.retrieveShuttleActivity(userContext.shuttleId)
                } catch (ex: Exception) {
                    attributes.addFlashAttribute("error", "Activity does not exist")
                    return "redirect:/menu"
                }
                return "on-assignment"
            } else {
                return "redirect:/menu"
            }
        } else throw AuthenticationException()
    }

    @RequestMapping("/end-assignment")
    fun endAssignment(attributes: RedirectAttributes, model: Model, http: HttpServletRequest) : String {
        if (authService.isAuthenticated(http)) {
            if (authService.isShuttleActive(http)) {
                val userContext = authService.getUserContext(http)

                // TBC : Check to see if shuttle activity exists.
                // TBC : If so, delete assignment id from activity, but do NOT mark assignment COMPLETED
                // TBC : If not, throw error and return to menu
                try {
                    val activity = activeService.retrieveShuttleActivity(userContext.shuttleId)

                    if (activity.assignmentID != 0) {
                        onRouteService.endAssignment(activity.assignmentID, true)
                    }
                } catch (ex: Exception) {
                    attributes.addFlashAttribute("error", "Activity does not exist")
                    return "redirect:/menu"
                }

                return "redirect:/select-assignment"
            } else {
                return "redirect:/menu"
            }
        } else throw AuthenticationException()
    }

    @RequestMapping("/finish-assignment")
    fun finishAssignment(attributes: RedirectAttributes, model: Model, http: HttpServletRequest) : String {
        if (authService.isAuthenticated(http)) {
            if (authService.isShuttleActive(http)) {
                val userContext = authService.getUserContext(http)

                // TBC : Check to see if shuttle activity exists.
                // TBC : If so, delete assignment id from activity AND mark assignment COMPLETED
                // TBC : If not, throw error and return to menu
                try {
                    val activity = activeService.retrieveShuttleActivity(userContext.shuttleId)

                    if (activity.assignmentID != 0) {
                        onRouteService.endAssignment(activity.assignmentID, false)
                    }
                } catch (ex: Exception) {
                    attributes.addFlashAttribute("error", "Activity does not exist")
                    return "redirect:/menu"
                }

                return "redirect:/select-assignment"
            } else {
                return "redirect:/menu"
            }
        } else throw AuthenticationException()
    }
}