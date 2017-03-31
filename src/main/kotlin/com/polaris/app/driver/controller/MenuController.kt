package com.polaris.app.driver.controller

import com.polaris.app.driver.controller.exception.AuthenticationException
import com.polaris.app.driver.service.AuthenticationService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping
import javax.servlet.http.HttpServletRequest

@Controller
class MenuController(private val authService: AuthenticationService) {

    @RequestMapping("/menu")
    fun menu(model: Model, http: HttpServletRequest) : String {
        if (authService.isAuthenticated(http)) {
            val userContext = authService.getUserContext(http)
            model.addAttribute("username", userContext.username)
            return "menu"
        } else {
            throw AuthenticationException("Error: user logged out")
        }
    }
}