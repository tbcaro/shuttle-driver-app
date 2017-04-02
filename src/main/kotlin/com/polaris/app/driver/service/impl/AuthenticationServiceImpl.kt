package com.polaris.app.driver.service.impl

import com.polaris.app.driver.controller.exception.AuthenticationException
import com.polaris.app.driver.repository.UserRepository
import com.polaris.app.driver.repository.UserType
import com.polaris.app.driver.service.AuthenticationService
import com.polaris.app.driver.service.bo.UserContext
import java.util.*
import javax.servlet.http.HttpServletRequest


class AuthenticationServiceImpl(val userRepository: UserRepository) : AuthenticationService {
    val SERVICE_ID = "serviceId"
    val SERVICE_CODE = "serviceCode"
    val USER_ID = "userId"
    val USERNAME = "username"
    val SHUTTLE_ID = "shuttleId"

    override fun authenticate(http: HttpServletRequest, username: String, password: String, servicecode: String) {
        val users = userRepository.findUserByLogin(username, password, servicecode)
        if (users.isEmpty()) {
            throw AuthenticationException("Authentication failed: invalid username, password, or servicecode")
        } else if (users.size > 1) {
            throw AuthenticationException("Authentication failed: more than one user for this combo. Please contact your administrator.")
        } else {
            val user = users[0]
            if (user.userType == UserType.DRIVER) {
                val sessionAttributes = HashMap<String, Any>()
                sessionAttributes.put(SERVICE_ID, user.serviceId)
                sessionAttributes.put(SERVICE_CODE, servicecode)
                sessionAttributes.put(USER_ID, user.id)
                sessionAttributes.put(USERNAME, user.userName)

                this.generateSession(http, sessionAttributes)
            }
        }
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

        // TBC : Max inactive session: 5 mins
        session.maxInactiveInterval = 5 * 60
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

            val user = UserContext()
            (it.getAttribute(SERVICE_ID) as? Int)?.let { user.serviceId = it }
            (it.getAttribute(SERVICE_CODE) as? String)?.let { user.servicecode = it }
            (it.getAttribute(USER_ID) as? Int)?.let { user.userId = it }
            (it.getAttribute(USERNAME) as? String)?.let { user.username = it }
            (it.getAttribute(SHUTTLE_ID) as? Int)?.let { user.shuttleId = it }

            return user
        }

        throw Exception("Not authenticated")
    }

    override fun isShuttleActive(http: HttpServletRequest): Boolean {
        val session = http.getSession(false)
        session?.let {
            try {
                var user = this.getUserContext(http)
                if (user.shuttleId > 0) return true
            } catch (ex: Exception) { }
        }

        return false
    }

    override fun addShuttleId(http: HttpServletRequest, shuttleId: Int) {
        val session = http.getSession(false)

        session?.let {
            session.setAttribute(SHUTTLE_ID, shuttleId)
        } ?: throw Exception("Not authenticated")
    }

    override fun deleteShuttleId(http: HttpServletRequest) {
        val session = http.getSession(false)
        session?.let {
            session.removeAttribute(SHUTTLE_ID)
        } ?: throw Exception("Not authenticated")
    }
}