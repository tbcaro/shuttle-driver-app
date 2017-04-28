package com.polaris.app.driver.repository.pg

import com.polaris.app.driver.repository.SimulationRepository
import com.polaris.app.driver.service.bo.SimCycleEntity
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component

@Component
class SimulationPgRepository(val db: JdbcTemplate) : SimulationRepository {
    override fun deleteSimByShuttleId(shuttleId: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun findSimCyclesByShuttleId(shuttleId: Int): List<SimCycleEntity> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun storeSimCycles(cycles: List<SimCycleEntity>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}