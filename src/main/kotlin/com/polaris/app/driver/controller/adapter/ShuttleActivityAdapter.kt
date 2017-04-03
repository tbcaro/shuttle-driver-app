package com.polaris.app.driver.controller.adapter

import com.polaris.app.dispatch.controller.adapter.enums.ShuttleState
import java.math.BigDecimal


class ShuttleActivityAdapter {
    var shuttleId: Int = 0
    var driverId: Int = 0
    var assignmentId: Int = 0
    var latitude: BigDecimal = BigDecimal("0")
    var longitude: BigDecimal = BigDecimal("0")
    var currentStopIndex: Int = 0
    var heading: BigDecimal = BigDecimal("0")
    var status: ShuttleState = ShuttleState.NONE
}