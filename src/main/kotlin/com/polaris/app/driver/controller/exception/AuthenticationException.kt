package com.polaris.app.driver.controller.exception


class AuthenticationException(private val msg : String = "User logged out") : Exception(msg)