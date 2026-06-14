package com.example.demo.controller

import com.example.demo.model.Budget
import com.example.demo.model.User
import com.example.demo.service.BudgetService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal

data class BudgetRequest(
    val category: String,
    val limitAmount: BigDecimal,
    val month: String
)

@RestController
@RequestMapping("/api/budgets")
class BudgetController(private val budgetService: BudgetService) {

    private fun currentUserId(): Long {
        val user = SecurityContextHolder.getContext().authentication.principal as User
        return user.id
    }

    @GetMapping
    fun getBudgets(@RequestParam month: String) =
        ResponseEntity.ok(budgetService.getBudgets(currentUserId(), month))

    @PostMapping
    fun createBudget(@RequestBody req: BudgetRequest) =
        ResponseEntity.ok(budgetService.createBudget(currentUserId(), req.category, req.limitAmount, req.month))

    @DeleteMapping("/{id}")
    fun deleteBudget(@PathVariable id: Long): ResponseEntity<Void> {
        budgetService.deleteBudget(id, currentUserId())
        return ResponseEntity.noContent().build()
    }
}