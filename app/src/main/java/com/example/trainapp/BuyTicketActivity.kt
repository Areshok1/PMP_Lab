package com.example.trainapp

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

class BuyTicketActivity : AppCompatActivity() {

    private var selectedSeat: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buy_ticket)

        val dateButton: Button = findViewById(R.id.date_button)
        val dateTextView: TextView = findViewById(R.id.date_text_view)
        val routesSpinner: Spinner = findViewById(R.id.routes_spinner)
        val categorySpinner: Spinner = findViewById(R.id.category_spinner)
        val selectSeatButton: Button = findViewById(R.id.select_seat_button)
        val buyTicketButton: Button = findViewById(R.id.buy_ticket_button)
        val showMapButton: Button = findViewById(R.id.show_map_button)

        val routes = listOf(
            "Київ - Львів",
            "Київ - Одеса",
            "Київ - Харків",
            "Київ - Дніпро",
            "Київ - Ужгород"
        )

        val categories = listOf("Плацкарт", "Купе", "Люкс")

        val routesAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, routes)
        val categoryAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)

        routesSpinner.adapter = routesAdapter
        categorySpinner.adapter = categoryAdapter

        dateButton.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                dateTextView.text = selectedDate
            }, year, month, day)

            datePickerDialog.show()
        }

        selectSeatButton.setOnClickListener {
            val selectedRoute = routesSpinner.selectedItem.toString()
            val selectedDate = dateTextView.text.toString()
            val selectedCategory = categorySpinner.selectedItem.toString()

            val intent = Intent(this, SelectSeatActivity::class.java).apply {
                putExtra("route", selectedRoute)
                putExtra("date", selectedDate)
                putExtra("category", selectedCategory)
            }
            startActivityForResult(intent, 1)
        }

        buyTicketButton.setOnClickListener {
            val selectedRoute = routesSpinner.selectedItem.toString()
            val selectedDate = dateTextView.text.toString()
            val selectedCategory = categorySpinner.selectedItem.toString()

            if (selectedSeat != null && selectedDate.isNotEmpty()) {
                saveTicket(selectedRoute, selectedDate, selectedCategory, selectedSeat!!)
                val intent = Intent(this, UserProfileActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Будь ласка, оберіть місце та дату", Toast.LENGTH_SHORT).show()
            }
        }

        showMapButton.setOnClickListener {
            val selectedRoute = routesSpinner.selectedItem.toString()
            if (selectedRoute.isNotEmpty()) {
                val intent = Intent(this, RouteMapActivity::class.java).apply {
                    putExtra("selectedRoute", selectedRoute)
                }
                startActivity(intent)
            } else {
                Toast.makeText(this, "Будь ласка, оберіть маршрут", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK) {
            selectedSeat = data?.getIntExtra("selectedSeat", -1)
        }
    }

    private fun saveTicket(route: String, date: String, category: String, seat: Int) {
        val sharedPreferences = getSharedPreferences("TrainApp", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        val ticketsJson = sharedPreferences.getString("tickets", null)
        val tickets = if (ticketsJson != null) {
            val type = object : TypeToken<MutableList<Ticket>>() {}.type
            Gson().fromJson<MutableList<Ticket>>(ticketsJson, type)
        } else {
            mutableListOf()
        }

        tickets.add(Ticket(route, date, category, seat))

        val newTicketsJson = Gson().toJson(tickets)
        editor.putString("tickets", newTicketsJson)
        editor.apply()
    }

    data class Ticket(val route: String, val date: String, val category: String, val seat: Int)
}
