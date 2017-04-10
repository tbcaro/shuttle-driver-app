package com.polaris.app.driver.repository.pg

import com.polaris.app.driver.repository.LoginRepository
import com.polaris.app.driver.repository.UserType
import com.polaris.app.driver.repository.entity.UserEntity
import com.polaris.app.driver.service.bo.Login
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component

@Component
class LoginPgRepository(val db: JdbcTemplate): LoginRepository {
    override fun checkCredentials(login: Login): UserEntity {
        val userEntities = db.query(
                "SELECT service.serviceid, \"user\".\"ID\", \"user\".fname, \"user\".lname, \"user\".username, \"user\".usertype FROM service INNER JOIN driver ON (service.serviceid = driver.serviceid) INNER JOIN \"user\" ON (driver.\"ID\" = \"user\".\"ID\")WHERE service.servicecode = ? AND \"user\".username = ? AND \"user\".\"Password\" = ? AND service.isactive = true AND driver.isactive = true AND driver.isarchived = false;",
                arrayOf(login.serviceCode, login.username, login.password),
                {
                    resultSet, rowNum -> UserEntity(
                        resultSet.getInt("ID"),
                        resultSet.getInt("serviceid"),
                        resultSet.getString("username"),
                        resultSet.getString("fname"),
                        resultSet.getString("lname"),
                        UserType.valueOf(resultSet.getString("usertype"))
                    )
                }

        )
        return userEntities[0]
    }

}