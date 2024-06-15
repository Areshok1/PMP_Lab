package com.example.trainapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var currentDateTimeTextView: TextView
    private lateinit var handler: Handler
    private lateinit var runnable: Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        currentDateTimeTextView = findViewById(R.id.current_date_time)
        handler = Handler(Looper.getMainLooper())

        runnable = object : Runnable {
            override fun run() {
                val currentDateTimeString = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(Date())
                currentDateTimeTextView.text = currentDateTimeString
                handler.postDelayed(this, 1000)
            }
        }

        handler.post(runnable)

        val viewRoutesButton: Button = findViewById(R.id.view_routes_button)
        val buyTicketButton: Button = findViewById(R.id.buy_ticket_button)
        val userProfileButton: Button = findViewById(R.id.user_profile_button)

        viewRoutesButton.setOnClickListener {
            val intent = Intent(this, RoutesActivity::class.java)
            startActivity(intent)
        }

        buyTicketButton.setOnClickListener {
            val intent = Intent(this, BuyTicketActivity::class.java)
            startActivity(intent)
        }

        userProfileButton.setOnClickListener {
            val intent = Intent(this, UserProfileActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(runnable)
    }
}
