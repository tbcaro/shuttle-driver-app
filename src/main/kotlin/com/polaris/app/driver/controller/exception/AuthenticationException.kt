package com.polaris.app.driver.controller.exception


class AuthenticationException(private val msg : String = "UserEntity not logged in") : Exception(msg)