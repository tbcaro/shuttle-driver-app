package com.polaris.app.driver.service

import com.polaris.app.driver.service.bo.SimCycle


interface SimulationService {
    fun saveSimulation(cycles: List<SimCycle>)
    fun loadSimulation(shuttleId: Int): List<SimCycle>
}