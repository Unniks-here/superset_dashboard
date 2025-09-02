package com.example.supersetdashboard

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * MainActivity provides simple form inputs for Superset configuration
 * and requests a guest token from a backend service.
 */
class MainActivity : AppCompatActivity() {

    // Replace with your own backend base URL that issues guest tokens.
    private val backendBaseUrl = "https://myserver.com/"

    private lateinit var tokenService: TokenService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Build Retrofit client for backend calls
        val retrofit = Retrofit.Builder()
            .baseUrl(backendBaseUrl)
            .client(OkHttpClient.Builder().build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        tokenService = retrofit.create(TokenService::class.java)

        val editBaseUrl = findViewById<EditText>(R.id.editBaseUrl)
        val editDashboardId = findViewById<EditText>(R.id.editDashboardId)
        val editUserId = findViewById<EditText>(R.id.editUserId)
        val editTenantId = findViewById<EditText>(R.id.editTenantId)
        val editRole = findViewById<EditText>(R.id.editRole)
        val btnOpen = findViewById<Button>(R.id.btnOpen)

        btnOpen.setOnClickListener {
            val baseUrl = editBaseUrl.text.toString().trim()
            val dashboardId = editDashboardId.text.toString().trim()
            val userId = editUserId.text.toString().trim()
            val tenantId = editTenantId.text.toString().trim()
            val role = editRole.text.toString().trim()

            // Build request body to send to backend
            val request = TokenRequest(
                userId = userId,
                tenantId = tenantId,
                role = role,
                dashboardId = dashboardId,
                supersetBaseUrl = baseUrl
            )

            // Call backend to get guest token
            tokenService.getGuestToken(request).enqueue(object : Callback<TokenResponse> {
                override fun onResponse(
                    call: Call<TokenResponse>,
                    response: Response<TokenResponse>
                ) {
                    if (response.isSuccessful) {
                        val token = response.body()?.token
                        if (!token.isNullOrEmpty()) {
                            openDashboard(baseUrl, dashboardId, token)
                        } else {
                            Toast.makeText(this@MainActivity, "Invalid token", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@MainActivity, "Token request failed", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<TokenResponse>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "Network error: ${'$'}{t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    /**
     * Start DashboardActivity passing all required information.
     */
    private fun openDashboard(baseUrl: String, dashboardId: String, token: String) {
        val intent = Intent(this, DashboardActivity::class.java).apply {
            putExtra(DashboardActivity.EXTRA_BASE_URL, baseUrl)
            putExtra(DashboardActivity.EXTRA_DASHBOARD_ID, dashboardId)
            putExtra(DashboardActivity.EXTRA_GUEST_TOKEN, token)
        }
        startActivity(intent)
    }
}
