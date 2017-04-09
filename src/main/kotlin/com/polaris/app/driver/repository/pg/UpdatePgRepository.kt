package com.polaris.app.driver.repository.pg

import com.polaris.app.driver.repository.UpdateRepository
import com.polaris.app.driver.service.bo.UpdateShuttle
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component

@Component
class UpdatePgRepository(val db: JdbcTemplate): UpdateRepository {
    override fun pushShuttleActivity(shuttle: UpdateShuttle) {
        db.update(
                "UPDATE shuttle_activity SET heading = ? AND latitude = ? AND longitude = ? WHERE shuttleid = ?;",
                arrayOf(shuttle.heading, shuttle.latitude, shuttle.longitude, shuttle.shuttleID)
        )
    }

}