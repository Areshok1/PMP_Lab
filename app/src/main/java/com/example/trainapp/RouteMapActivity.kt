package com.example.trainapp

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity

class RouteMapActivity : AppCompatActivity() {

    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_route_map)

        webView = findViewById(R.id.webView)
        webView.settings.javaScriptEnabled = true
        webView.webViewClient = WebViewClient()
        webView.loadUrl("file:///android_asset/map.html")

        val selectedRoute = intent.getStringExtra("selectedRoute")
        val routes = mapOf(
            "Київ - Львів" to listOf(
                Pair(50.4501, 30.5234), // Київ
                Pair(49.8397, 24.0297)  // Львів
            ),
            "Київ - Одеса" to listOf(
                Pair(50.4501, 30.5234), // Київ
                Pair(46.4825, 30.7233)  // Одеса
            ),
            "Київ - Харків" to listOf(
                Pair(50.4501, 30.5234), // Київ
                Pair(49.9935, 36.2304)  // Харків
            ),
            "Київ - Дніпро" to listOf(
                Pair(50.4501, 30.5234), // Київ
                Pair(48.4647, 35.0462)  // Дніпро
            ),
            "Київ - Ужгород" to listOf(
                Pair(50.4501, 30.5234), // Київ
                Pair(48.6208, 22.2879)  // Ужгород
            )
        )

        val routeCoordinates = routes[selectedRoute]

        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                routeCoordinates?.let {
                    val jsCoordinates = it.joinToString(",") { coord -> "[${coord.first},${coord.second}]" }
                    webView.evaluateJavascript("showRoute([$jsCoordinates])", null)
                }
            }
        }
    }
}
