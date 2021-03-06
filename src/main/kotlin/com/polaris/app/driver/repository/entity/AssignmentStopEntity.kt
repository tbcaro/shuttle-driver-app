package com.polaris.app.driver.repository.entity

import java.math.BigDecimal
import java.time.LocalDateTime

data class AssignmentStopEntity(
        val assignmentStopID: Int,
        val assignmentID: Int,
        val index: Int,
        val ETA: LocalDateTime?,
        val ETD: LocalDateTime?,
        val TOA: LocalDateTime?,
        val TOD: LocalDateTime?,
        val stopID: Int?,
        val stopName: String,
        val address: String,
        val latitude: BigDecimal,
        val longitude: BigDecimal
)