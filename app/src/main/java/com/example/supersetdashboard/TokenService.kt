package com.example.supersetdashboard

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Retrofit interface for backend guest token endpoint.
 * The backend should sign the guest token with Superset's secret key.
 */
interface TokenService {
    @POST("get_guest_token")
    fun getGuestToken(@Body request: TokenRequest): Call<TokenResponse>
}
