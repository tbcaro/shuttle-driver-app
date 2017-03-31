package com.polaris.app.driver.repository.pg

import com.polaris.app.driver.repository.UserRepository
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component

@Component
class UserPgRepository(val db: JdbcTemplate) : UserRepository {
}