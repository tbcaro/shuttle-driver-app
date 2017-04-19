package com.polaris.app.driver.repository.pg

import com.polaris.app.dispatch.controller.adapter.enums.ShuttleState
import com.polaris.app.driver.repository.ActiveRepository
import com.polaris.app.driver.repository.entity.AssignmentEntity
import com.polaris.app.driver.repository.entity.AssignmentStopEntity
import com.polaris.app.driver.repository.entity.ShuttleActivityEntity
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.sql.Date
import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime


@Component
class ActivePgRepository(val db: JdbcTemplate): ActiveRepository {
    override fun findAssignment(assignmentID: Int): AssignmentEntity {
        val assignments = db.query(
                "SELECT * FROM assignment WHERE assignmentid = ? AND isarchived = false",
                arrayOf(assignmentID),
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
        val rows = db.queryForList(
                "SELECT * FROM assignment_stop LEFT OUTER JOIN stop ON (assignment_stop.stopid = stop.\"ID\") WHERE assignmentid = ? ORDER BY \"Index\";",
                assignmentID
        )

        val assignmentStopEntities = arrayListOf<AssignmentStopEntity>()
        rows.forEach {
            var latitude = BigDecimal("0")
            var longitude = BigDecimal("0")
            var name = ""
            var address = ""
            var stopId = (it["stopid"]?.let { it as Int })

            if (stopId == null) {
                latitude = (it["assignment_stop.latitude"]?.let { it as BigDecimal }) ?: BigDecimal("0")
                longitude = (it["assignment_stop.longitude"]?.let { it as BigDecimal }) ?: BigDecimal("0")
                address = (it["assignment_stop.address"]?.let { it as String }) ?: ""
                name = "Custom Stop"
            } else {
                latitude = (it["stop.latitude"]?.let { it as BigDecimal }) ?: BigDecimal("0")
                longitude = (it["stop.longitude"]?.let { it as BigDecimal }) ?: BigDecimal("0")
                address = (it["stop.address"]?.let { it as String }) ?: ""
                name = (it["Name"]?.let { it as String }) ?: ""
            }

            val assignmentStop = AssignmentStopEntity(
                    assignmentStopID = (it["assignment_stop_id"]?.let { it as Int }) ?: 0,
                    assignmentID = (it["assignmentid"]?.let { it as Int }) ?: 0,
                    index = (it["index"]?.let { it as Int }) ?: 0,
                    ETA = (it["estimatedtimeofarrival"]?.let { it as Timestamp })?.toLocalDateTime(),
                    ETD = (it["estimatedtimeofdeparture"]?.let { it as Timestamp })?.toLocalDateTime(),
                    TOA = (it["timeofarrival"]?.let { it as Timestamp })?.toLocalDateTime(),
                    TOD = (it["timeofdeparture"]?.let { it as Timestamp })?.toLocalDateTime(),
                    stopID = stopId,
                    stopName = name,
                    address = address,
                    latitude = latitude,
                    longitude = longitude
            )

            assignmentStopEntities.add(assignmentStop)
        }
        return assignmentStopEntities
    }

    override fun beginRoute(shuttleID: Int, assignmentID: Int) {
        db.update(
                "UPDATE assignment SET status = 'IN_PROGRESS' WHERE assignmentid = ?;",
                assignmentID
        )
        db.update(
                "UPDATE shuttle_activity SET status = 'DRIVING', assignmentid = ? WHERE shuttleid = ?;",
                assignmentID, shuttleID
        )
    }

    override fun endService(shuttleID: Int) {
        val activity = this.findShuttleActivity(shuttleID)

        if (activity.assignmentID != null) {
            db.update(
                    "UPDATE assignment SET status = 'UNFINISHED' WHERE assignmentid = ?;",
                    activity.assignmentID
            )
        }
        db.update(
                "DELETE FROM shuttle_activity WHERE shuttleid = ?;",
                shuttleID
        )
    }

    override fun findAssignments(driverID: Int, shuttleID: Int, startDate: LocalDate): List<AssignmentEntity> {
        val assignments = db.query(
                "SELECT * FROM assignment " +
                        "LEFT OUTER JOIN route ON (route.\"ID\" = assignment.routeid) " +
                        "WHERE driverid = ? AND shuttleid = ? AND startdate = ? AND (status = 'SCHEDULED' OR status = 'UNFINISHED') AND assignment.isarchived = false ORDER BY starttime;",
                arrayOf(driverID, shuttleID, Date.valueOf(startDate)),
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

    override fun findShuttleActivity(serviceID: Int): ShuttleActivityEntity {
        val sa = db.query(
                "SELECT * FROM shuttle_activity WHERE shuttleid = ?",
                arrayOf(serviceID),{
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