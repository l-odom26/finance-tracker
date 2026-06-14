package com.example.demo.service

import com.example.demo.model.TransactionType
import com.example.demo.repository.TransactionRepository
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDate

data class CategorySummary(
    val category: String,
    val spent: BigDecimal
)

data class DashboardResponse(
    val month: String,
    val totalIncome: BigDecimal,
    val totalExpenses: BigDecimal,
    val netBalance: BigDecimal,
    val categories: List<CategorySummary>
)

@Service
class DashboardService(private val transactionRepository: TransactionRepository) {

    fun getDashboard(userId: Long, month: String): DashboardResponse {
        // Parse month string e.g. "2026-06"
        val yearMonth = month.split("-")
        val year = yearMonth[0].toInt()
        val monthNum = yearMonth[1].toInt()

        val from = LocalDate.of(year, monthNum, 1)
        val to = from.withDayOfMonth(from.lengthOfMonth())

        val transactions = transactionRepository.findByUserIdAndDateBetween(userId, from, to)

        val totalIncome = transactions
            .filter { it.type == TransactionType.INCOME }
            .fold(BigDecimal.ZERO) { acc, t -> acc + t.amount }

        val totalExpenses = transactions
            .filter { it.type == TransactionType.EXPENSE }
            .fold(BigDecimal.ZERO) { acc, t -> acc + t.amount }

        val categories = transactions
            .filter { it.type == TransactionType.EXPENSE }
            .groupBy { it.category }
            .map { (category, txns) ->
                CategorySummary(
                    category = category,
                    spent = txns.fold(BigDecimal.ZERO) { acc, t -> acc + t.amount }
                )
            }

        return DashboardResponse(
            month = month,
            totalIncome = totalIncome,
            totalExpenses = totalExpenses,
            netBalance = totalIncome - totalExpenses,
            categories = categories
        )
    }
}