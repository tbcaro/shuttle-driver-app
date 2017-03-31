package com.polaris.app.driver.service

import com.polaris.app.driver.repository.UserRepository
import com.polaris.app.driver.service.bo.UserContext
import javax.servlet.http.HttpServletRequest


interface AuthenticationService {
    fun authenticate(http: HttpServletRequest, username: String, password: String, servicecode: String)
    fun isAuthenticated(http: HttpServletRequest) : Boolean
    fun generateSession(http: HttpServletRequest, attributes: Map<String, Any>)
    fun invalidateSession(http: HttpServletRequest)
    fun getUserContext(http: HttpServletRequest) : UserContext
}