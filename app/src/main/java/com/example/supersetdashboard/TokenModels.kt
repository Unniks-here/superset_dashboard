package com.example.supersetdashboard

/**
 * Request object sent to backend to mint a guest token.
 */
data class TokenRequest(
    val userId: String,
    val tenantId: String,
    val role: String,
    val dashboardId: String,
    val supersetBaseUrl: String
)

/**
 * Response from backend containing the signed guest token.
 */
data class TokenResponse(
    val token: String
)
