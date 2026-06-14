package com.example.demo.repository

import com.example.demo.model.Budget
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDate

interface BudgetRepository : JpaRepository<Budget, Long> {
    fun findByUserIdAndMonth(userId: Long, month: LocalDate): List<Budget>
    fun findByUserIdAndMonthAndCategory(userId: Long, month: LocalDate, category: String): Budget?
}