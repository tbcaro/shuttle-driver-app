package com.polaris.app.driver.repository

import com.polaris.app.driver.repository.entity.UserEntity


interface UserRepository {
    fun findUserByLogin(username: String, password: String, servicecode: String) : List<UserEntity>
}