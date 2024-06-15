package com.example.trainapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView

class RoutesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_routes)

        val routesListView: ListView = findViewById(R.id.routes_list_view)
        val routes = listOf(
            "Київ - Львів",
            "Київ - Одеса",
            "Київ - Харків",
            "Київ - Дніпро",
            "Київ - Ужгород"
        )

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, routes)
        routesListView.adapter = adapter
    }
}
