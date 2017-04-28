package com.polaris.app.driver.service.bo

import com.polaris.app.dispatch.controller.adapter.enums.ShuttleState
import java.math.BigDecimal


data class SimCycleEntity(
    var shuttleId: Int?,
    var driverId: Int?,
    var index: Int?,
    var latitude: BigDecimal?,
    var longitude: BigDecimal?,
    var heading: BigDecimal?,
    var status: ShuttleState?
)