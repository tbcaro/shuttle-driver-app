package com.polaris.app.driver.repository

import com.polaris.app.driver.repository.entity.ShuttleActivityEntity
import com.polaris.app.driver.service.bo.UpdateShuttle

interface UpdateRepository{
    fun pushShuttleActivity(shuttle: UpdateShuttle)
    fun checkPreviousState(shuttle: UpdateShuttle): UpdateType
    fun pushStopData(shuttle: UpdateShuttle, type: UpdateType)
    fun findShuttleActivity(shuttleID: Int): ShuttleActivityEntity
}