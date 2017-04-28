package com.polaris.app.driver.repository.pg

import com.polaris.app.dispatch.controller.adapter.enums.ShuttleState
import com.polaris.app.driver.controller.adapter.enums.AssignmentStatus
import com.polaris.app.driver.repository.SimulationRepository
import com.polaris.app.driver.repository.entity.AssignmentEntity
import com.polaris.app.driver.service.bo.SimCycleEntity
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component
import java.math.BigDecimal

@Component
class SimulationPgRepository(val db: JdbcTemplate) : SimulationRepository {
    override fun deleteSimByShuttleId(shuttleId: Int) {
        db.update(
                "DELETE FROM simulation_cycle WHERE shuttleid = ?",
                shuttleId
        )
    }

    override fun findSimCyclesByShuttleId(shuttleId: Int): List<SimCycleEntity> {
        val simCycles = db.query(
                "SELECT * FROM simulation_cycle WHERE shuttleId = ?",
                arrayOf(shuttleId),
                {
                    rs, row -> SimCycleEntity(
                        shuttleId = rs.getInt("shuttleid"),
                        driverId = rs.getInt("driverid"),
                        index = rs.getInt("index"),
                        latitude = rs.getBigDecimal("latitude"),
                        longitude = rs.getBigDecimal("longitude"),
                        heading = rs.getBigDecimal("heading"),
                        status = ShuttleState.valueOf(rs.getString("status"))
                    )
                }
        )
        return simCycles
    }

    override fun storeSimCycles(cycles: List<SimCycleEntity>) {
        cycles.forEach {
            db.update(
                    "INSERT INTO simulation_cycle " +
                            "(shuttleid, driverid, index, latitude, longitude, heading, status) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?::shuttle_status);",
                    it.shuttleId, it.driverId, it.index, it.latitude, it.longitude, it.heading, it.status
            )
        }
    }
}