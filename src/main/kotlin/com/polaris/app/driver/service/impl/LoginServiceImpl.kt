package com.polaris.app.driver.service.impl

import com.polaris.app.driver.repository.LoginRepository
import com.polaris.app.driver.service.LoginService
import com.polaris.app.driver.service.bo.Login
import com.polaris.app.driver.service.bo.User

class LoginServiceImpl(val loginRepository: LoginRepository): LoginService {
    override fun checkCredentials(serviceCode: String, username: String, password: String): User {
        val login = Login(
                serviceCode = serviceCode,
                username = username,
                password = password
        )
        val userEntity = this.loginRepository.checkCredentials(login)

        return User(
                serviceid = userEntity.serviceid,
                userid = userEntity.userid,
                username = userEntity.username,
                fname = userEntity.fname,
                lname = userEntity.lname,
                usertype = userEntity.usertype
        )
    }

    override fun checkCredentials(login: Login): User {
        val userEntity = this.loginRepository.checkCredentials(login)

        return User(
                serviceid = userEntity.serviceid,
                userid = userEntity.userid,
                username = userEntity.username,
                fname = userEntity.fname,
                lname = userEntity.lname,
                usertype = userEntity.usertype
        )
    }


}