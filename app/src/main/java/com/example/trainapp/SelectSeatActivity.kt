package com.example.trainapp

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.GridLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SelectSeatActivity : AppCompatActivity() {

    private val seatStatus = mutableMapOf<Int, String>()
    private var selectedSeat: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_seat)

        val seatGrid: GridLayout = findViewById(R.id.seat_grid)
        val confirmButton: Button = findViewById(R.id.confirm_button)

        val route = intent.getStringExtra("route")
        val date = intent.getStringExtra("date")
        val category = intent.getStringExtra("category")

        // Initialize seat status
        for (i in 1..28) {
            seatStatus[i] = "available"
        }

        // Load booked seats from SharedPreferences
        val sharedPreferences = getSharedPreferences("TrainApp", Context.MODE_PRIVATE)
        val ticketsJson = sharedPreferences.getString("tickets", null)
        if (ticketsJson != null) {
            val type = object : TypeToken<List<BuyTicketActivity.Ticket>>() {}.type
            val tickets: List<BuyTicketActivity.Ticket> = Gson().fromJson(ticketsJson, type)
            for (ticket in tickets) {
                if (ticket.route == route && ticket.date == date && ticket.category == category) {
                    seatStatus[ticket.seat] = "booked"
                }
            }
        }

        // Dynamically add seat buttons
        for (i in 1..28) {
            val seatButton = Button(this)
            seatButton.text = String.format("%02d", i)
            seatButton.setBackgroundColor(if (seatStatus[i] == "booked") Color.RED else Color.GREEN)
            seatButton.isEnabled = seatStatus[i] == "available"
            seatButton.setOnClickListener {
                onSeatSelected(i, seatButton)
            }
            seatGrid.addView(seatButton)
        }

        confirmButton.setOnClickListener {
            if (selectedSeat != null) {
                val resultIntent = Intent()
                resultIntent.putExtra("selectedSeat", selectedSeat)
                setResult(RESULT_OK, resultIntent)
                finish()
            } else {
                Toast.makeText(this, "No seat selected", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun onSeatSelected(seatNumber: Int, seatButton: Button) {
        if (seatStatus[seatNumber] == "available") {
            if (selectedSeat != null) {
                val previousSeatButton = findViewById<Button>(selectedSeat!!)
                previousSeatButton.setBackgroundColor(Color.GREEN)
            }
            selectedSeat = seatNumber
            seatButton.setBackgroundColor(Color.YELLOW)
        } else {
            Toast.makeText(this, "Seat $seatNumber is not available", Toast.LENGTH_SHORT).show()
        }
    }
}
