package com.polaris.app.driver.controller

import com.polaris.app.driver.controller.exception.AuthenticationException
import com.polaris.app.driver.service.AuthenticationService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping
import javax.servlet.http.HttpServletRequest

@Controller
class AppController(private val authService: AuthenticationService) {

    @RequestMapping("/app")
    fun app(model: Model, http: HttpServletRequest) : String {
        if (authService.isAuthenticated(http)) return "app" else throw AuthenticationException("Error: user logged out")
    }
}