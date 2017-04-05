package com.polaris.app.driver.service.bo

import com.polaris.app.driver.repository.UserType

data class User(
        val userid: Int,
        val serviceid: Int,
        val username: String,
        val fname: String,
        val lname: String,
        val usertype: UserType
)