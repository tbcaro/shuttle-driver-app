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
class MenuController(private val authService: AuthenticationService, private val inactiveService: InactiveService) {

    @RequestMapping("/menu")
    fun menu(attributes: RedirectAttributes, model: Model, http: HttpServletRequest) : String {
        attributes.asMap().forEach { model.addAttribute(it.key, it.value) }
        if (authService.isAuthenticated(http)) {
            val userContext = authService.getUserContext(http)
            val shuttles = inactiveService.retrieveShuttles(userContext.serviceId)
            var shuttleOptions: MutableMap<Int, String> = HashMap()

            shuttles.forEach {
                shuttleOptions.put(it.shuttleID, it.shuttleName)
            }

            model.addAttribute("shuttleOptions", shuttleOptions)

            if (authService.isShuttleActive(http)) {
                val shuttleActivityExists = true // TBC : TODO : check to see if that shuttle activity exists.

                if (shuttleActivityExists)
                    model.addAttribute("isInService", true)
                else
                    null// TBC : TODO : If it doesn't, delete the shuttle activity from their session.
            } else {
                model.addAttribute("isInService", false)
            }
            return "menu"
        } else throw AuthenticationException("Error: user logged out")
    }
}