package com.polaris.app.driver.repository.pg

import com.polaris.app.driver.repository.InactiveRepository
import com.polaris.app.driver.repository.entity.CheckEntity
import com.polaris.app.driver.repository.entity.InactiveShuttleEntity
import com.polaris.app.driver.service.bo.ActiveShuttle
import com.polaris.app.driver.service.bo.InactiveShuttle
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component

@Component
class InactivePgRepository(val db: JdbcTemplate): InactiveRepository {
    override fun findShuttles(serviceid: Int): List<InactiveShuttleEntity> {
        val inactiveShuttleEntities = db.query(
                "SELECT \"ID\", \"Name\" FROM shuttle WHERE serviceid = ? AND isarchived = false and isactive = true;",
                arrayOf(serviceid),
                {
                    resultSet, rowNum -> InactiveShuttleEntity(
                        resultSet.getInt("ID"),
                        resultSet.getString("Name")
                )
                }
        )
        return inactiveShuttleEntities
    }

    override fun checkShuttle(shuttle: Int): Boolean {
        val checkEntities = db.query(
                "SELECT * FROM shuttle_activity WHERE shuttleid = ?",
                arrayOf(shuttle),
                {
                    resultSet, rowNum -> CheckEntity(
                        resultSet.getInt("shuttleid")
                )
                }
        )//Should return true if the given shuttle does not have any current shuttle_activity
        return checkEntities == null
    }

    override fun checkDriver(driver: Int): Boolean {
        val checkEntities = db.query(
                "SELECT * FROM shuttle_activity WHERE driverid = ?;",
                arrayOf(driver),
                {
                    resultSet, rowNum -> CheckEntity(
                        resultSet.getInt("shuttleid")
                )
                }
        )//Should return true if the given driver does not have any current shuttle_activity
        return checkEntities == null    }

    override fun beginService(s: ActiveShuttle) {
        db.update(
                "INSERT INTO shuttle_activity (shuttleid, latitude, longitude, status, driverid, heading) VALUES (?, ?, ?, 'ACTIVE', ?);",
                arrayOf(s.shuttleID, s.latitude, s.longitude,s.status, s.driverID, s.heading)
        )
    }
}