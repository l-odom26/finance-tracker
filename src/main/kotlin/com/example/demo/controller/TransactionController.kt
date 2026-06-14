package com.example.demo.controller

import com.example.demo.model.TransactionType
import com.example.demo.service.TransactionService
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal
import java.time.LocalDate

data class TransactionRequest(
    val amount: BigDecimal,
    val type: TransactionType,
    val description: String = "",
    val date: LocalDate,
    val category: String = ""
)

@RestController
@RequestMapping("/api/transactions")
class TransactionController(private val transactionService: TransactionService) {

    private val userId = 1L // temporary, will replace with JWT later

    @GetMapping
    fun getAll(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) from: LocalDate?,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) to: LocalDate?
    ) = ResponseEntity.ok(transactionService.getAll(userId, from, to))

    @PostMapping
    fun create(@RequestBody req: TransactionRequest) =
        ResponseEntity.ok(transactionService.create(
            userId, req.amount, req.type, req.description, req.date, req.category))

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody req: TransactionRequest) =
        ResponseEntity.ok(transactionService.update(
            id, userId, req.amount, req.type, req.description, req.date, req.category))

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<Void> {
        transactionService.delete(id, userId)
        return ResponseEntity.noContent().build()
    }
}
