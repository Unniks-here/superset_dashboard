package com.example.supersetdashboard

import android.os.Bundle
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity

/**
 * Displays a Superset dashboard inside a WebView using the Superset Embedded SDK.
 * The HTML template is stored in assets/embed.html and placeholders are replaced
 * with runtime values.
 */
class DashboardActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_BASE_URL = "extra_base_url"
        const val EXTRA_DASHBOARD_ID = "extra_dashboard_id"
        const val EXTRA_GUEST_TOKEN = "extra_guest_token"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val baseUrl = intent.getStringExtra(EXTRA_BASE_URL) ?: ""
        val dashboardId = intent.getStringExtra(EXTRA_DASHBOARD_ID) ?: ""
        val guestToken = intent.getStringExtra(EXTRA_GUEST_TOKEN) ?: ""

        val webView = findViewById<WebView>(R.id.webview)

        // Configure WebView for JS and mobile-friendly settings
        val settings: WebSettings = webView.settings
        settings.javaScriptEnabled = true
        settings.domStorageEnabled = true
        settings.cacheMode = WebSettings.LOAD_NO_CACHE
        settings.useWideViewPort = true
        settings.loadWithOverviewMode = true

        // Ensure links stay within the WebView
        webView.webViewClient = WebViewClient()

        // Load the local HTML file and inject runtime values
        val html = assets.open("embed.html").bufferedReader().use { it.readText() }
            .replace("{{DASHBOARD_ID}}", dashboardId)
            .replace("{{BASE_URL}}", baseUrl)
            .replace("{{GUEST_TOKEN}}", guestToken)

        webView.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null)
    }
}
