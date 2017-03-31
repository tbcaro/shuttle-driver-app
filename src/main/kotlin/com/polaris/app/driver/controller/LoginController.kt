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

    val TEST_SERVICE_CODE = "servicecode1"
    val TEST_USER = "username1"
    val TEST_PASSWORD = "password1"

    @RequestMapping("/")
    fun root(model: Model) : String {
        return "redirect:/loginForm"
    }

    @RequestMapping("/loginForm")
    fun loginForm(model: Model, attributes: RedirectAttributes) : String {
        attributes.asMap().forEach { model.addAttribute(it.key, it.value) }
        return "login"
    }

    @RequestMapping("/login")
    fun login(attributes: RedirectAttributes, http: HttpServletRequest, loginAdapter: LoginAdapter) : String {

        loginAdapter.let {
            if (it.servicecode == TEST_SERVICE_CODE &&
                it.username == TEST_USER &&
                it.password == TEST_PASSWORD
            ) {
                val sessionAttributes = HashMap<String, Any>()
                sessionAttributes.put("serviceCode", TEST_SERVICE_CODE)
                sessionAttributes.put("username", TEST_USER)

                authService.generateSession(http, sessionAttributes)
            }
        }

        if (authService.isAuthenticated(http)) {
            return "redirect:/menu"
        } else {
            throw AuthenticationException("login failed: service code, username, password combination invalid")
//            attributes.addFlashAttribute("error", "login failed: service code, username, password combination invalid")
//            return "redirect:/loginForm"
        }
    }

    @RequestMapping("/logout")
    fun logout(attributes: RedirectAttributes, http: HttpServletRequest) : String {
        authService.invalidateSession(http)
        attributes.addFlashAttribute("success", "logout successful")
        return "redirect:/loginForm"
    }
}