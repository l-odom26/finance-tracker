package com.example.demo.config

import com.example.demo.repository.UserRepository
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtFilter(
    private val userRepository: UserRepository,
    @Value("\${jwt.secret}") private val jwtSecret: String
) : OncePerRequestFilter() {

    private val secretKey = Keys.hmacShaKeyFor(jwtSecret.toByteArray())

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader = request.getHeader("Authorization")

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            val token = authHeader.substring(7)
            try {
                val claims = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .payload

                val email = claims.subject
                val user = userRepository.findByEmail(email).orElse(null)

                if (user != null) {
                    val auth = UsernamePasswordAuthenticationToken(
                        user, null, emptyList()
                    )
                    SecurityContextHolder.getContext().authentication = auth
                }
            } catch (e: Exception) {
            }
        } else {
        }

        filterChain.doFilter(request, response)
    }
}