package com.polaris.app.driver.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping
import javax.servlet.http.HttpServletRequest

@Controller
class MenuController {

    @RequestMapping("/menu")
    fun menu(model: Model, http: HttpServletRequest) : String {
        return "menu"
    }
}