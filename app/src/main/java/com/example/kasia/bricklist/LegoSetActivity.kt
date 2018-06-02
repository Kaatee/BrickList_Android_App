package com.example.kasia.bricklist

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView

class LegoSetActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lego_set)

        val inventoryName: String = getIntent().getStringExtra("name")
        val database = DataBaseHelper(this)

        val listView: ListView = findViewById<ListView>(R.id.listView)
        var inventoriesPart: ArrayList<InventoriesPart> = java.util.ArrayList()
        inventoriesPart = database.getInventoriesParts(inventoryName)

        var adapter = MyCustomAdapter(this, inventoriesPart, this)
        listView?.adapter = adapter
        adapter.notifyDataSetChanged()
    }




}
