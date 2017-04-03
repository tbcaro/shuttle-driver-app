package com.polaris.app.driver.repository

import com.polaris.app.driver.repository.entity.UserEntity
import com.polaris.app.driver.service.bo.Login

interface LoginRepository{
    fun checkCredentials(login: Login): UserEntity
}