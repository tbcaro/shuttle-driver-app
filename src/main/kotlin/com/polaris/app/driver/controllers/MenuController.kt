package com.polaris.app.driver.controllers

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

@Controller
class MenuController {

    val MENU_PAGE = "menu"

    @RequestMapping("/menu")
    fun menu() : String {
        return MENU_PAGE
    }
}