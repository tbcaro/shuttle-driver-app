package com.polaris.app.driver.controllers

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

@Controller
class ScheduleController {

    val SCHEDULE_PAGE = "schedule"

    @RequestMapping("/schedule")
    fun schedule() : String {
        return SCHEDULE_PAGE
    }
}