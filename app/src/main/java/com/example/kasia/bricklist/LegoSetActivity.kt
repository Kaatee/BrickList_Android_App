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
        Log.i("---", "Tu jestem 6")
        inventoriesPart = database.getInventoriesParts(inventoryName)

        Log.i("---", "Tu jestem 7")
        var adapter = MyCustomAdapter(this, inventoriesPart, this)
        Log.i("---", "Tu jestem 8")
        listView?.adapter = adapter
        Log.i("---", "Tu jestem 9")
        adapter.notifyDataSetChanged()
        Log.i("---", "Tu jestem 10")
    }




}
