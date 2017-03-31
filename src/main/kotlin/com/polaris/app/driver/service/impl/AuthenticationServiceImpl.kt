package com.polaris.app.driver.service.impl

import com.polaris.app.driver.repository.UserRepository
import com.polaris.app.driver.service.AuthenticationService
import com.polaris.app.driver.service.bo.UserContext
import javax.servlet.http.HttpServletRequest


class AuthenticationServiceImpl(val userRepository: UserRepository) : AuthenticationService {
    val SERVICE_ID = "serviceId"
    val SERVICE_CODE = "serviceCode"
    val USER_ID = "userId"
    val USERNAME = "username"

    override fun authenticate(servicecode: String, username: String, password: String) {
        // TBC : TODO : Find user by servicecode, username, password combo
            // TBC : TODO : If valid user, generate session
            // TBC : TODO : Else, throw exception
    }

    override fun isAuthenticated(http: HttpServletRequest): Boolean {
        val session = http.getSession(false)

        session?.let {
            try {
                var user = this.getUserContext(http)
                if (user.username.isNotBlank() && user.servicecode.isNotBlank()) return true
            } catch (ex: Exception) { }
        }

        return false
    }

    override fun generateSession(http: HttpServletRequest, attributes: Map<String, Any>) {
        val session = http.getSession(true)

        attributes.entries.forEach {
            session.setAttribute(it.key, it.value)
        }

        // TBC : Max inactive session: 30 mins
        session.maxInactiveInterval = 30 * 60
    }

    override fun invalidateSession(http: HttpServletRequest) {
        val session = http.getSession(false)
        session?.let {
            it.invalidate()
        }
    }

    override fun getUserContext(http: HttpServletRequest) : UserContext {
        val session = http.getSession(false)
        session?.let {
            val serviceId = it.getAttribute(SERVICE_ID) as? Int
            val servicecode = it.getAttribute(SERVICE_CODE) as? String
            val userId = it.getAttribute(USER_ID) as? Int
            val username = it.getAttribute(USERNAME) as? String

            val user = UserContext()
            serviceId?.let { user.serviceId = it }
            servicecode?.let { user.servicecode = it }
            userId?.let { user.userId = it }
            username?.let { user.username = it }

            return user
        }

        throw Exception("Not authenticated")
    }
}