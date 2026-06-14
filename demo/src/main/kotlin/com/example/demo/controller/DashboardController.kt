package com.example.demo.controller

import com.example.demo.model.User
import com.example.demo.service.DashboardService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/dashboard")
class DashboardController(private val dashboardService: DashboardService) {

    private fun currentUserId(): Long {
        val user = SecurityContextHolder.getContext().authentication.principal as User
        return user.id
    }

    @GetMapping
    fun getDashboard(@RequestParam month: String): ResponseEntity<Any> {
        return ResponseEntity.ok(dashboardService.getDashboard(currentUserId(), month))
    }
}