package com.polaris.app.driver.service.impl

import com.polaris.app.dispatch.controller.adapter.enums.ShuttleState
import com.polaris.app.driver.repository.SimulationRepository
import com.polaris.app.driver.service.SimulationService
import com.polaris.app.driver.service.bo.SimCycle
import com.polaris.app.driver.service.bo.SimCycleEntity
import java.math.BigDecimal


class SimulationServiceImpl(val simulationRepository: SimulationRepository) : SimulationService {
    override fun saveSimulation(cycles: List<SimCycle>) {
        val cycleEntities = arrayListOf<SimCycleEntity>()
        if (cycles.size > 0) {
            val cyclesShuttleId = cycles[0].shuttleId
            cyclesShuttleId?.let { simulationRepository.deleteSimByShuttleId(cyclesShuttleId) }

            cycles.forEach {
                val entity = SimCycleEntity(
                        shuttleId = it.shuttleId,
                        driverId = it.driverId,
                        index = it.index,
                        latitude = it.latitude,
                        longitude = it.longitude,
                        heading = it.heading,
                        status = it.status
                )

                cycleEntities.add(entity)
            }
            simulationRepository.storeSimCycles(cycleEntities)
        } else {
            throw Exception("No cycles to save")
        }
    }

    override fun loadSimulation(shuttleId: Int): List<SimCycle> {
        val cycleEntities = simulationRepository.findSimCyclesByShuttleId(shuttleId)
        val cycles = arrayListOf<SimCycle>()
        cycleEntities.forEach {
            val cycle = SimCycle(
                    shuttleId = it.shuttleId,
                    driverId = it.driverId,
                    index = it.index,
                    latitude = it.latitude,
                    longitude = it.longitude,
                    heading = it.heading,
                    status = it.status
            )

            cycles.add(cycle)
        }
        return cycles
    }
}