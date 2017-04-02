package com.polaris.app.driver.controller

import com.polaris.app.driver.controller.adapter.LoginAdapter
import com.polaris.app.driver.controller.exception.AuthenticationException
import com.polaris.app.driver.service.AuthenticationService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpSession

@Controller
class LoginController(private val authService: AuthenticationService) {

    @RequestMapping("/")
    fun root(model: Model) : String {
        return "redirect:/login"
    }

    @RequestMapping("/loginForm")
    fun loginForm(model: Model, attributes: RedirectAttributes) : String {
        attributes.asMap().forEach { model.addAttribute(it.key, it.value) }
        return "login"
    }

    @RequestMapping("/login")
    fun login(attributes: RedirectAttributes, http: HttpServletRequest, loginAdapter: LoginAdapter) : String {
        var loginAttempted = false
        if (!loginAdapter.username.isNullOrBlank() && !loginAdapter.password.isNullOrBlank() && !loginAdapter.servicecode.isNullOrBlank()) {
            authService.authenticate(http, loginAdapter.username!!, loginAdapter.password!!, loginAdapter.servicecode!!)
            loginAttempted = true
        }

        if (authService.isAuthenticated(http)) {
            return "redirect:/menu"
        } else if (loginAttempted){
            throw AuthenticationException("login failed: service code, username, password combination invalid")
        } else {
            return "redirect:/loginForm"
        }
    }

    @RequestMapping("/logout")
    fun logout(attributes: RedirectAttributes, http: HttpServletRequest) : String {
        authService.invalidateSession(http)
        attributes.addFlashAttribute("success", "logout successful")
        return "redirect:/loginForm"
    }
}