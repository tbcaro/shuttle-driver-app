package com.polaris.app.driver

import com.polaris.app.driver.repository.pg.UserPgRepository
import com.polaris.app.driver.service.AuthenticationService
import com.polaris.app.driver.service.impl.AuthenticationServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.session.SessionAutoConfiguration
import org.springframework.context.annotation.Bean

@SpringBootApplication(exclude = arrayOf(SessionAutoConfiguration::class))
open class Application {
    @Autowired
    lateinit var userRepo: UserPgRepository

    @Bean
    open fun authService(): AuthenticationService {
        return AuthenticationServiceImpl(userRepo)
    }
}

fun main(args: Array<String>) {
    SpringApplication.run(Application::class.java, *args)
}


