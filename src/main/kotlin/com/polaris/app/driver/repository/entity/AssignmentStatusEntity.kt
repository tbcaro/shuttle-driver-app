package com.polaris.app.driver.repository.entity

import com.polaris.app.driver.controller.adapter.enums.AssignmentStatus
import java.time.LocalTime

data class AssignmentStatusEntity(
        val TOA: LocalTime?
)