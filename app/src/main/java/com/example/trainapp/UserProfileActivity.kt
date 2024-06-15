package com.example.trainapp

import android.content.Context
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class UserProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        val ticketsListView: ListView = findViewById(R.id.tickets_list_view)

        val sharedPreferences = getSharedPreferences("TrainApp", Context.MODE_PRIVATE)
        val ticketsJson = sharedPreferences.getString("tickets", null)
        val tickets = if (ticketsJson != null) {
            val type = object : TypeToken<List<BuyTicketActivity.Ticket>>() {}.type
            Gson().fromJson<List<BuyTicketActivity.Ticket>>(ticketsJson, type)
        } else {
            emptyList()
        }

        val ticketStrings = tickets.map { ticket ->
            "Маршрут: ${ticket.route}, Дата: ${ticket.date}, Категорія: ${ticket.category}, Місце: ${ticket.seat}"
        }

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, ticketStrings)
        ticketsListView.adapter = adapter
    }
}
