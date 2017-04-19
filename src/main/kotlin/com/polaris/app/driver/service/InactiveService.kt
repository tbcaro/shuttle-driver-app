package com.polaris.app.driver.service

import com.polaris.app.driver.service.bo.ActiveShuttle
import com.polaris.app.driver.service.bo.InactiveShuttle
import com.polaris.app.driver.service.bo.ShuttleActivity
import java.math.BigDecimal

interface InactiveService{
    fun retrieveShuttles(serviceid: Int): List<InactiveShuttle>
    fun beginActiveService(shuttleID: Int, driverID: Int)
    fun retrieveShuttleActivity(shuttleID: Int): ShuttleActivity
}