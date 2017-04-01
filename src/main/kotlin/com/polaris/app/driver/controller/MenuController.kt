package com.polaris.app.driver.controller

import com.polaris.app.driver.controller.exception.AuthenticationException
import com.polaris.app.driver.service.AuthenticationService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping
import java.util.*
import javax.servlet.http.HttpServletRequest

@Controller
class MenuController(private val authService: AuthenticationService) {

    @RequestMapping("/menu")
    fun menu(model: Model, http: HttpServletRequest) : String {
        if (authService.isAuthenticated(http)) {
            var shuttleOptions: MutableMap<Int, String> = HashMap()

            shuttleOptions.put(1, "Shuttle 1A")
            shuttleOptions.put(2, "Shuttle 2B")
            shuttleOptions.put(3, "Shuttle 3C")

            model.addAttribute("shuttleOptions", shuttleOptions)
            return "menu"
        } else throw AuthenticationException("Error: user logged out")
    }
}