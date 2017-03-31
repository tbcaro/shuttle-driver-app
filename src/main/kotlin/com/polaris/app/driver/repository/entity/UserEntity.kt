package com.polaris.app.driver.repository.entity

import com.polaris.app.driver.repository.UserType


data class UserEntity(
        val id: Int,
        val serviceId: Int,
        val firstName: String,
        val lastName: String,
        val userName: String,
        val userType: UserType
)