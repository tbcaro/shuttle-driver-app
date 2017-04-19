package com.polaris.app.driver.repository.entity

import com.polaris.app.dispatch.controller.adapter.enums.ShuttleState

data class StatusCheckEntity(
        val shuttleID: Int,
        val index: Int,
        val status: ShuttleState
)