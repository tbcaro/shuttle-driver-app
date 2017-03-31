package com.polaris.app.driver.controller.exception


class AuthenticationException(private val msg : String = "User not logged in") : Exception(msg)