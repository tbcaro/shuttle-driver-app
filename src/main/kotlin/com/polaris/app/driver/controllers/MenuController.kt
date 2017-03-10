package com.polaris.app.driver.controllers

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping

@Controller
class MenuController {

    @RequestMapping("/menu")
    fun menu(model: Model) : String {
        return "menu"
    }
}