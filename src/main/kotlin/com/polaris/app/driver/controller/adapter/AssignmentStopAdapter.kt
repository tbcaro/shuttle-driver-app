package com.polaris.app.driver.controller.adapter

import java.math.BigDecimal
import java.time.LocalTime
import java.time.temporal.ChronoUnit


class AssignmentStopAdapter {
    var assingmentStopId: Int = 0
    var stopId: Int = 0
    var order: Int = 0
    var name: String = ""
    var address: String = ""
    var estArriveTime: LocalTime = LocalTime.now()
    var estDepartTime: LocalTime? = LocalTime.now()
}