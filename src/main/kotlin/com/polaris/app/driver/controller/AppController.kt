package com.polaris.app.driver.controller

import com.polaris.app.driver.service.AuthenticationService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping

@Controller
class AppController(private val authService: AuthenticationService) {

    @RequestMapping("/app")
    fun app(model: Model) : String {
        return "app"
    }
}