package com.example.demo.service

import com.example.demo.model.Transaction
import com.example.demo.model.TransactionType
import com.example.demo.repository.TransactionRepository
import com.example.demo.repository.UserRepository
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDate

@Service
class TransactionService(
    private val transactionRepository: TransactionRepository,
    private val userRepository: UserRepository
) {
    fun getAll(userId: Long, from: LocalDate?, to: LocalDate?): List<Transaction> {
        return if (from != null && to != null)
            transactionRepository.findByUserIdAndDateBetween(userId, from, to)
        else
            transactionRepository.findByUserId(userId)
    }

    fun create(userId: Long, amount: BigDecimal, type: TransactionType,
               description: String, date: LocalDate, category: String): Transaction {
        val user = userRepository.findById(userId)
            .orElseThrow { RuntimeException("User not found") }
        return transactionRepository.save(
            Transaction(user = user, amount = amount, type = type,
                description = description, date = date, category = category)
        )
    }

    fun update(id: Long, userId: Long, amount: BigDecimal, type: TransactionType,
               description: String, date: LocalDate, category: String): Transaction {
        val transaction = transactionRepository.findById(id)
            .orElseThrow { RuntimeException("Transaction not found") }
        if (transaction.user.id != userId) throw RuntimeException("Unauthorized")
        return transactionRepository.save(
            transaction.copy(amount = amount, type = type,
                description = description, date = date, category = category)
        )
    }

    fun delete(id: Long, userId: Long) {
        val transaction = transactionRepository.findById(id)
            .orElseThrow { RuntimeException("Transaction not found") }
        if (transaction.user.id != userId) throw RuntimeException("Unauthorized")
        transactionRepository.delete(transaction)
    }
}
