package com.polaris.app.driver.controller

import com.polaris.app.driver.controller.exception.AuthenticationException
import com.polaris.app.driver.service.AuthenticationService
import com.polaris.app.driver.service.InactiveService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import java.util.*
import javax.servlet.http.HttpServletRequest

@Controller
class SimulationController(private val authService: AuthenticationService, private val inactiveService: InactiveService) {

    @RequestMapping("/simulator")
    fun simulator(attributes: RedirectAttributes, model: Model, http: HttpServletRequest) : String {
        if (authService.isAuthenticated(http)) {
            val userContext = authService.getUserContext(http)
            val shuttles = inactiveService.retrieveShuttles(userContext.serviceId)
            var shuttleOptions: MutableMap<Int, String> = HashMap()

            shuttles.forEach {
                shuttleOptions.put(it.shuttleID, it.shuttleName)
            }

            model.addAttribute("shuttleOptions", shuttleOptions)

            return "simulator"
        } else throw AuthenticationException()
    }
}