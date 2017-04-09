package com.polaris.app.driver.service

import com.polaris.app.driver.service.bo.Login
import com.polaris.app.driver.service.bo.User

interface LoginService{
    //Two version of checkCredentials, use whichever is more convenient, feel free to delete the other
    fun checkCredentials(serviceCode: String, username: String, password: String): User
    fun checkCredentials(login: Login): User
}