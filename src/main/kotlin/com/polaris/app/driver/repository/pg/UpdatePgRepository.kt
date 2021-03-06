package com.polaris.app.driver.repository.pg

import com.polaris.app.dispatch.controller.adapter.enums.ShuttleState
import com.polaris.app.driver.repository.UpdateRepository
import com.polaris.app.driver.repository.UpdateType
import com.polaris.app.driver.repository.entity.ShuttleActivityEntity
import com.polaris.app.driver.repository.entity.StatusCheckEntity
import com.polaris.app.driver.service.bo.UpdateShuttle
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component
import java.sql.Timestamp
import java.time.LocalDateTime

@Component
class UpdatePgRepository(val db: JdbcTemplate): UpdateRepository {
    override fun pushShuttleActivity(shuttle: UpdateShuttle) {
        db.update(
                "UPDATE shuttle_activity SET heading = ?, latitude = ?, longitude = ?, \"Index\" = ?, status = ?::shuttle_status WHERE shuttleid = ?;",
                shuttle.heading, shuttle.latitude, shuttle.longitude, shuttle.index, shuttle.status.toString(), shuttle.shuttleID
        )
    }

    override fun checkPreviousState(shuttle: UpdateShuttle): UpdateType {
        val prevState = db.query(
                "SELECT * FROM shuttle_activity WHERE shuttleid = ?",
                arrayOf(shuttle.shuttleID),{
                    resultSet, rowNum -> StatusCheckEntity(
                        resultSet.getInt("shuttleid"),
                        resultSet.getInt("Index"),
                        status = ShuttleState.valueOf(resultSet.getString("status"))
                    )
                }
        )

        if (prevState.isNotEmpty() && prevState[0].status != shuttle.status) {
            if (prevState[0].index != shuttle.index){
                return UpdateType.DEPART
            }
            else{
                return UpdateType.ARRIVE
            }
        }
        else{
            return UpdateType.NORMAL
        }
    }

    override fun pushStopData(shuttle: UpdateShuttle, type: UpdateType) {
        if (type == UpdateType.ARRIVE) {
            db.update(
                "UPDATE assignment_stop SET timeofarrival = ? WHERE assignmentid = ? AND \"Index\" = ?;",
                Timestamp.valueOf(LocalDateTime.now()), shuttle.assignmentID, shuttle.index
            )
        }
        else if (type == UpdateType.DEPART) {
            db.update(
                "UPDATE assignment_stop SET timeofdeparture = ? WHERE assignmentid = ? AND \"Index\" = ?;",
                Timestamp.valueOf(LocalDateTime.now()), shuttle.assignmentID, shuttle.index - 1
            )
        }
    }

    override fun findShuttleActivity(shuttleID: Int): ShuttleActivityEntity {
        val sa = db.query(
                "SELECT * FROM shuttle_activity WHERE shuttleid = ?",
                arrayOf(shuttleID),{
            resultSet, rowNum -> ShuttleActivityEntity(
                resultSet.getInt("shuttleid"),
                resultSet.getInt("driverid"),
                resultSet.getInt("assignmentid"),
                resultSet.getInt("Index"),
                resultSet.getBigDecimal("latitude"),
                resultSet.getBigDecimal("longitude"),
                resultSet.getBigDecimal("heading"),
                status = ShuttleState.valueOf(resultSet.getString("status"))
        )
        }
        )
        return sa[0]
    }

}