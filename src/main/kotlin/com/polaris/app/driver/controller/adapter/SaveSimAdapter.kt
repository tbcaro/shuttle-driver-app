package com.polaris.app.driver.controller.adapter

import com.polaris.app.driver.service.bo.SimCycle


class SaveSimAdapter{
    var cycles: List<SimAdapter> = arrayListOf()

    fun toSimCycleList(driverId: Int): List<SimCycle> {
        val simCycles = arrayListOf<SimCycle>()
        this.cycles.forEachIndexed { i, simAdapter ->
            simCycles.add(
                    SimCycle(
                            shuttleId = simAdapter.shuttleId,
                            driverId = driverId,
                            index = i,
                            latitude = simAdapter.latitude,
                            longitude = simAdapter.longitude,
                            heading = simAdapter.heading,
                            status = simAdapter.status
                    )
            )
        }

        return simCycles
    }
}