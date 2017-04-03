package com.polaris.app.driver.repository.entity

data class UserEntity(
        val serviceid: Int,
        val userid: Int,
        val username: String,
        val fname: String,
        val lname: String,
        val usertype: String
)