package com.polaris.app.driver.repository

import com.polaris.app.driver.service.bo.SimCycleEntity


interface SimulationRepository {
    fun findSimCyclesByShuttleId(shuttleId: Int)
    fun deleteSimByShuttleId(shuttleId: Int)
    fun storeSimCycles(cycles: List<SimCycleEntity>)
}