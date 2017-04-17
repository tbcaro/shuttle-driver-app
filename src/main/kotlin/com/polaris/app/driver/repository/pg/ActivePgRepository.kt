package com.polaris.app.driver.repository.pg

import com.polaris.app.driver.repository.ActiveRepository
import com.polaris.app.driver.repository.entity.AssignmentEntity
import com.polaris.app.driver.repository.entity.AssignmentStopEntity
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component
import java.time.LocalDate


@Component
class ActivePgRepository(val db: JdbcTemplate): ActiveRepository {
    override fun findAssignment(driverID: Int, shuttleID: Int, startDate: LocalDate): AssignmentEntity {
        val assignments = db.query(
                "SELECT * FROM assignment WHERE driverid = ? AND shuttleid = ? AND startdate = ? AND status = 'SCHEDULED' AND isarchived = false ORDER BY starttime;",
                arrayOf(driverID, shuttleID, startDate),
                {
                    resultSet, rowNum -> AssignmentEntity(
                        resultSet.getInt("assignmentid"),
                        resultSet.getInt("serviceid"),
                        resultSet.getInt("driverid"),
                        resultSet.getInt("shuttleid"),
                        resultSet.getInt("routeid"),
                        resultSet.getTimestamp("starttime").toLocalDateTime().toLocalTime(),
                        resultSet.getTimestamp("startdate").toLocalDateTime().toLocalDate(),
                        resultSet.getString("routename"),
                        resultSet.getString("status")
                    )
                }
        )
        return assignments[0]
    }

    override fun findAssignmentStops(assignmentID: Int): List<AssignmentStopEntity> {
        val stops = db.query(
                "SELECT * FROM assignment_stop WHERE assignmentid = ? ORDER BY \"Index\";",
                arrayOf(assignmentID),
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
    }

    override fun beginRoute(shuttleID: Int, assignmentID: Int) {
        db.update(
                "UPDATE assignment SET status = 'IN_PROGRESS' WHERE assignmentid = ?;",
                assignmentID
        )
        db.update(
                "UPDATE shuttle_activity SET status = 'DRIVING' AND assignmentid = ? WHERE shuttleid = ?;",
                assignmentID, shuttleID
        )
    }

    override fun endService(shuttleID: Int) {
        db.update(
                "DELETE FROM shuttle_activity WHERE shuttleid = ?;",
                shuttleID
        )
    }

    override fun findAssignments(driverID: Int, shuttleID: Int, startDate: LocalDate): List<AssignmentEntity> {
        val assignments = db.query(
                "SELECT * FROM assignment WHERE driverid = ? AND shuttleid = ? AND startdate = ? AND status = 'SCHEDULED' AND isarchived = false ORDER BY starttime;",
                arrayOf(driverID, shuttleID, startDate),
                {
                    resultSet, rowNum -> AssignmentEntity(
                        resultSet.getInt("assignmentid"),
                        resultSet.getInt("serviceid"),
                        resultSet.getInt("driverid"),
                        resultSet.getInt("shuttleid"),
                        resultSet.getInt("routeid"),
                        resultSet.getTimestamp("starttime").toLocalDateTime().toLocalTime(),
                        resultSet.getTimestamp("startdate").toLocalDateTime().toLocalDate(),
                        resultSet.getString("routename"),
                        resultSet.getString("status")
                )
                }
        )
        return assignments
    }
}