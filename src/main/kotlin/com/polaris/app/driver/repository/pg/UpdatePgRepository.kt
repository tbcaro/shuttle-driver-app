package com.polaris.app.driver.repository.pg

import com.polaris.app.dispatch.controller.adapter.enums.ShuttleState
import com.polaris.app.driver.repository.UpdateRepository
import com.polaris.app.driver.repository.UpdateType
import com.polaris.app.driver.repository.entity.StatusCheckEntity
import com.polaris.app.driver.service.bo.UpdateShuttle
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class UpdatePgRepository(val db: JdbcTemplate): UpdateRepository {
    override fun pushShuttleActivity(shuttle: UpdateShuttle) {
        db.update(
                "UPDATE shuttle_activity SET heading = ? AND latitude = ? AND longitude = ? AND \"Index\" = ? AND status = ? WHERE shuttleid = ?;",
                shuttle.heading, shuttle.latitude, shuttle.longitude, shuttle.index, shuttle.status, shuttle.shuttleID
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
        if (prevState[0].status != shuttle.status){
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
                LocalDateTime.now(), shuttle.assignmentID, shuttle.index
            )
        }
        else if (type == UpdateType.DEPART) {
            db.update(
                "UPDATE assignment_stop SET timeofdeparture = ? WHERE assignmentid = ? AND \"Index\" = ?;",
                LocalDateTime.now(), shuttle.assignmentID, shuttle.index
            )
        }
    }

}