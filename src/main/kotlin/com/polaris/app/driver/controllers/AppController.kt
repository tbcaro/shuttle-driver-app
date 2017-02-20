package com.polaris.app.driver.controllers

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

@Controller
class AppController {

    val APP_PAGE = "app"

    @RequestMapping("/app")
    fun app() : String {
        return APP_PAGE
    }
}