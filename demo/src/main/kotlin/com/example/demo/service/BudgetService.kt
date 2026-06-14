package com.example.demo.service

import com.example.demo.model.Budget
import com.example.demo.model.TransactionType
import com.example.demo.repository.BudgetRepository
import com.example.demo.repository.TransactionRepository
import com.example.demo.repository.UserRepository
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDate

data class BudgetStatus(
    val id: Long,
    val category: String,
    val limitAmount: BigDecimal,
    val spent: BigDecimal,
    val remaining: BigDecimal,
    val overBudget: Boolean
)

@Service
class BudgetService(
    private val budgetRepository: BudgetRepository,
    private val transactionRepository: TransactionRepository,
    private val userRepository: UserRepository
) {
    fun getBudgets(userId: Long, month: String): List<BudgetStatus> {
        val monthDate = parseMonth(month)
        val to = monthDate.withDayOfMonth(monthDate.lengthOfMonth())
        val budgets = budgetRepository.findByUserIdAndMonth(userId, monthDate)
        val transactions = transactionRepository.findByUserIdAndDateBetween(userId, monthDate, to)

        return budgets.map { budget ->
            val spent = transactions
                .filter { it.type == TransactionType.EXPENSE && it.category == budget.category }
                .fold(BigDecimal.ZERO) { acc, t -> acc + t.amount }

            BudgetStatus(
                id = budget.id,
                category = budget.category,
                limitAmount = budget.limitAmount,
                spent = spent,
                remaining = budget.limitAmount - spent,
                overBudget = spent > budget.limitAmount
            )
        }
    }

    fun createBudget(userId: Long, category: String, limitAmount: BigDecimal, month: String): Budget {
        val user = userRepository.findById(userId).orElseThrow { RuntimeException("User not found") }
        val monthDate = parseMonth(month)
        return budgetRepository.save(
            Budget(user = user, category = category, limitAmount = limitAmount, month = monthDate)
        )
    }

    fun deleteBudget(id: Long, userId: Long) {
        val budget = budgetRepository.findById(id).orElseThrow { RuntimeException("Budget not found") }
        if (budget.user.id != userId) throw RuntimeException("Unauthorized")
        budgetRepository.delete(budget)
    }

    private fun parseMonth(month: String): LocalDate {
        val parts = month.split("-")
        return LocalDate.of(parts[0].toInt(), parts[1].toInt(), 1)
    }
}