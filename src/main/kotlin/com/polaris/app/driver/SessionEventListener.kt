package com.polaris.app.driver

import com.polaris.app.driver.repository.ActiveRepository
import org.springframework.stereotype.Component
import javax.servlet.http.HttpSessionEvent
import javax.servlet.http.HttpSessionListener

@Component
class SessionEventListener(
        private val activeRepository: ActiveRepository
) : HttpSessionListener {

    override fun sessionCreated(se: HttpSessionEvent?) {
        return
    }

    override fun sessionDestroyed(se: HttpSessionEvent?) {
        try {
            val shuttleId = se?.session?.getAttribute("shuttleId") as Int
            shuttleId.let { activeRepository.endService(it) }
        } catch (ignored: Exception) { }
    }
}