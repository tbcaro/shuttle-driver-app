package com.polaris.app.driver.repository.pg

import com.polaris.app.dispatch.controller.adapter.enums.ShuttleState
import com.polaris.app.driver.controller.adapter.enums.AssignmentStatus
import com.polaris.app.driver.repository.OnRouteRepository
import com.polaris.app.driver.repository.entity.*
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class OnRoutePgRepository(val db: JdbcTemplate): OnRouteRepository{
    /*override fun stop(assignmentid: Int, TOA: LocalDateTime, index: Int) {
        db.update(
                "UPDATE assignment_stop SET timeofarrival = ? WHERE assignmentid = ? AND \"Index\" = ?;",
                arrayOf(TOA, assignmentid, index)
        )
        db.update(
                "UPDATE shuttle_activity SET status = 'AT_STOP' WHERE assignmentid = ? AND \"Index\" = ?;",
                arrayOf(assignmentid, index)
        )
    }

    override fun driveUpdate(assignmentid: Int, TOD: LocalDateTime, index: Int) {
        db.update(
                "UPDATE assignment_stop SET timeofdeparture = ? WHERE assignmentid = ? AND \"Index\" = ?;",
                arrayOf(TOD, assignmentid, index)
        )

        db.update(
                "UPDATE shuttle_activity SET status = 'DRIVING' WHERE assignmentid = ? AND \"Index\" = ?;",
                arrayOf(assignmentid, index)
        )
    }

    override fun stopCheck(assignmentid: Int, index: Int): StopCheckEntity {
        val stopIndices = db.query(
                "SELECT * FROM assignment_stop WHERE assignmentid = ? AND \"Index\" > ? ORDER BY \"Index\";",
                arrayOf(assignmentid, index),
                {
                    resultSet, rowNum -> IndexEntity(
                        resultSet.getInt("Index")
                )
                }
        )
        return StopCheckEntity(//Returns the index of the next stop and how many stops remain
                index = stopIndices[0].index,
                remainingStops = stopIndices.size
        )
    }

    override fun updateStopData(assignmentid: Int, index: Int): List<AssignmentStopEntity> {
        val stops = db.query(
                "SELECT * FROM assignment_stop WHERE assignmentid = ? AND \"Index\" >= ? ORDER BY \"Index\";",
                arrayOf(assignmentid, index),
                {
                    resultSet, rowNum -> AssignmentStopEntity(
                        resultSet.getInt("assignment_stop_id"),
                        resultSet.getInt("assignmentid"),
                        resultSet.getInt("Index"),
                        resultSet.getTimestamp("estimatedtimeofarrival").toLocalDateTime(),
                        resultSet.getTimestamp("estimatedtimeofdeparture").toLocalDateTime(),
                        resultSet.getTimestamp("timeofarrival").toLocalDateTime(),
                        resultSet.getTimestamp("timeofdeparture").toLocalDateTime(),
                        resultSet.getInt("stopid"),
                        resultSet.getString("address"),
                        resultSet.getBigDecimal("latitude"),
                        resultSet.getBigDecimal("longitude")
                    )
                }
        )
        return stops
    }*/

    override fun endAssignment(assignmentid: Int) {
        db.update(
                "UPDATE assignment SET status = 'COMPLETED' WHERE assignmentid = ?;",
                assignmentid
        )
        db.update(
                "UPDATE shuttle_activity SET status = 'ACTIVE' AND assignmentid = null WHERE assignmentid = ?;",
                assignmentid
        )
    }

    override fun earlyEndAssignment(assignmentid: Int){
        db.update(
                "UPDATE assignment SET status = 'UNFINISHED' WHERE assignmentid = ?;",
                assignmentid
        )
        db.update(
                "UPDATE shuttle_activity SET status = 'ACTIVE' AND assignmentid = null WHERE assignmentid = ?;",
                assignmentid
        )
    }

    override fun checkAssignmentStatus(assignmentid: Int): Boolean {
        val TOA = db.query(
                "SELECT * FROM assignment_stop WHERE assignmentid = ?",
                arrayOf(assignmentid),{
                    resultSet, rowNum -> AssignmentStatusEntity(
                        resultSet.getInt("timeofarrival")
                    )
                }
        )
        TOA.forEach {
            if (it.TOA == null){
                return false
            }
        }
        return true
    }

    override fun endAssignmentWithTime(assignmentid: Int) {
        db.update(
                "UPDATE assignment SET status = 'COMPLETED' WHERE assignmentid = ?;",
                assignmentid
        )
        db.update(
                "UPDATE shuttle_activity SET status = 'ACTIVE' AND assignmentid = null AND timeofarrival = ? WHERE assignmentid = ?;",
                LocalDateTime.now(), assignmentid
        )
    }

    override fun findShuttleActivity(serviceID: Int): ShuttleActivityEntity {
        val sa = db.query(
                "SELECT * FROM shuttle_activity WHERE shuttleid = ?",
                arrayOf(serviceID),{
            resultSet, rowNum -> ShuttleActivityEntity(
                resultSet.getInt("shuttleid"),
                resultSet.getInt("driverid"),
                resultSet.getInt("assignmentid"),
                resultSet.getInt("assignment_stop_id"),
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