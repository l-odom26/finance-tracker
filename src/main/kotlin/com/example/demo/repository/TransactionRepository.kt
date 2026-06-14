package com.example.demo.repository

import com.example.demo.model.Transaction
import com.example.demo.model.TransactionType
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDate

interface TransactionRepository : JpaRepository<Transaction, Long> {
    fun findByUserId(userId: Long): List<Transaction>
    fun findByUserIdAndDateBetween(userId: Long, from: LocalDate, to: LocalDate): List<Transaction>
    fun findByUserIdAndType(userId: Long, type: TransactionType): List<Transaction>
}
