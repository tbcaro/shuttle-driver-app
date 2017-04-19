package com.polaris.app.driver.repository

import com.polaris.app.driver.repository.entity.InactiveShuttleEntity
import com.polaris.app.driver.repository.entity.ShuttleActivityEntity
import com.polaris.app.driver.service.bo.ActiveShuttle
import com.polaris.app.driver.service.bo.InactiveShuttle

interface InactiveRepository{
    fun findShuttles(serviceid: Int): List<InactiveShuttleEntity>
    fun checkShuttle(shuttle: Int): Boolean//Ensure the user is not trying to use a currently active shuttle
        //If this function returns false, do not allow them to begin service
        //If this function returns true, proceed to checkDriver
    fun checkDriver(driver: Int): Boolean//Ensure there are not two people actively using the same account
        //Should this function return false, warn user that their login info could be compromised
        //If this function returns true, proceed to beginService
    fun beginService(s: ActiveShuttle)
    fun findShuttleActivity(serviceID: Int): ShuttleActivityEntity
}