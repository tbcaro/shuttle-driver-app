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
class AppController(private val authService: AuthenticationService) {

    @RequestMapping("/activate")
    fun activate(model: Model, http: HttpServletRequest, activateAdapter: ActivateAdapter) : String {
        if (authService.isAuthenticated(http)) {
            var userContext = authService.getUserContext(http)

            println("Activating shuttle activity: ")
            println("shuttleId: ${activateAdapter.selectedShuttleId}")
            println("driverId: ${userContext.userId}")
            println("username: ${userContext.username}")
            println("servicecode: ${userContext.servicecode}")

            // TBC : TODO : Create shuttleActivity with shuttleId
            // TBC : TODO : Add shuttleId to session info

            // TBC : TODO : If successful, return app page
            // TBC : TODO : If failure, return menu with error

            return "app"
        } else throw AuthenticationException("Error: user logged out")
    }

    @RequestMapping("/app")
    fun app(model: Model, http: HttpServletRequest) : String {
        if (authService.isAuthenticated(http)) return "app" else throw AuthenticationException("Error: user logged out")
    }

    @RequestMapping("/deactivate")
    fun deactivate(model: Model, http: HttpServletRequest) : String {
        if (authService.isAuthenticated(http)) {
            // TBC : TODO : Delete shuttleActivity if one exists based on session info
            // TBC : TODO : Remove shuttleId from session info

            // TBC : TODO : If successful, return menu page
            // TBC : TODO : If failure, return menu with error

            return "menu"
        } else throw AuthenticationException("Error: user logged out")
    }
}