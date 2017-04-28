package com.polaris.app.driver.controller.adapter

import com.polaris.app.dispatch.controller.adapter.enums.ShuttleState
import java.math.BigDecimal


class SimAdapter {
    var shuttleId: Int? = null
    var latitude: BigDecimal? = null
    var longitude: BigDecimal? = null
    var heading: BigDecimal? = null
    var status: ShuttleState? = null
}