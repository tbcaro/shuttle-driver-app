package com.polaris.app.driver

import com.polaris.app.driver.repository.InactiveRepository
import com.polaris.app.driver.repository.pg.*
import com.polaris.app.driver.service.*
import com.polaris.app.driver.service.impl.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.session.SessionAutoConfiguration
import org.springframework.context.annotation.Bean

@SpringBootApplication(exclude = arrayOf(SessionAutoConfiguration::class))
open class Application {
    @Autowired
    lateinit var userRepo: UserPgRepository

    @Autowired
    lateinit var inactiveRepo: InactivePgRepository

    @Autowired
    lateinit var activeRepo: ActivePgRepository

    @Autowired
    lateinit var onRouteRepo: OnRoutePgRepository

    @Autowired
    lateinit var updateRepo: UpdatePgRepository

    @Bean
    open fun authService(): AuthenticationService {
        return AuthenticationServiceImpl(userRepo)
    }

    @Bean
    open fun inactiveService(): InactiveService {
        return InactiveServiceImpl(inactiveRepo)
    }

    @Bean
    open fun activeService(): ActiveService {
        return ActiveServiceImpl(activeRepo)
    }

    @Bean
    open fun onRouteService(): OnRouteService {
        return OnRouteServiceImpl(onRouteRepo)
    }

    @Bean
    open fun updateService(): UpdateService {
        return UpdateServiceImpl(updateRepo)
    }
}

fun main(args: Array<String>) {
    SpringApplication.run(Application::class.java, *args)
}


