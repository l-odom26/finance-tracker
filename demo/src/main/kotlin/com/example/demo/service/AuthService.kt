package com.example.demo.service

import com.example.demo.model.User
import com.example.demo.repository.UserRepository
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.util.Date

@Service
class AuthService(
    private val userRepository: UserRepository,
    @Value("\${jwt.secret}") private val jwtSecret: String
) {

    private val passwordEncoder = BCryptPasswordEncoder()
    private val secretKey = Keys.hmacShaKeyFor(jwtSecret.toByteArray())

    fun register(email: String, password: String, name: String): String {
        if (userRepository.findByEmail(email).isPresent)
            throw RuntimeException("Email already in use")
        val hashed = passwordEncoder.encode(password)
        val user = userRepository.save(User(email = email, password = hashed, name = name))
        return generateToken(user)
    }

    fun login(email: String, password: String): String {
        val user = userRepository.findByEmail(email)
            .orElseThrow { RuntimeException("Invalid email or password") }
        if (!passwordEncoder.matches(password, user.password))
            throw RuntimeException("Invalid email or password")
        return generateToken(user)
    }

    private fun generateToken(user: User): String {
        return Jwts.builder()
            .subject(user.email)
            .claim("userId", user.id)
            .issuedAt(Date())
            .expiration(Date(System.currentTimeMillis() + 86400000))
            .signWith(secretKey)
            .compact()
    }
}