package com.example.demo.controller

import com.example.demo.service.AuthService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

data class AuthRequest(val email: String, val password: String, val name: String = "")

@RestController
@RequestMapping("/api/auth")
class AuthController(private val authService: AuthService) {

    @PostMapping("/register")
    fun register(@RequestBody request: AuthRequest): ResponseEntity<Map<String, String>> {
        val token = authService.register(request.email, request.password, request.name)
        return ResponseEntity.ok(mapOf("token" to token))
    }

    @PostMapping("/login")
    fun login(@RequestBody request: AuthRequest): ResponseEntity<Map<String, String>> {
        val token = authService.login(request.email, request.password)
        return ResponseEntity.ok(mapOf("token" to token))
    }
}
