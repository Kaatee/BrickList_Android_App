package com.example.kasia.bricklist

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import kotlinx.android.synthetic.main.activity_lego_set.*

class LegoSetActivity : AppCompatActivity() {
    var inventoryName: String = ""
    var inventoriesPart: ArrayList<InventoriesPart> = java.util.ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lego_set)

        //val inventoryName: String = getIntent().getStringExtra("name")
        inventoryName = getIntent().getStringExtra("name")
        val database = DataBaseHelper(this)

        val listView: ListView = findViewById<ListView>(R.id.listView)
        //var inventoriesPart: ArrayList<InventoriesPart> = java.util.ArrayList()
        inventoriesPart = database.getInventoriesParts(inventoryName)

        var adapter = MyCustomAdapter(this, inventoriesPart, this)
        listView?.adapter = adapter
        adapter.notifyDataSetChanged()


        //write brick which is not collected to XML file
        writeToXML.setOnClickListener{
            Log.i("---", " Nacisniety buttom wtiteToXML")
            val database = DataBaseHelper(this)
            //Log.i("---", " Stworzona baza danych")
            var items: ArrayList<InventoriesPart> = database.getInventoriesParts(inventoryName)
            database.close()
            var x = WriteToXML(items, this)
            try {
                x.writeXML(items)
            } catch(e:Exception){Log.i("---", "Blad: "+e.message )}

            val intent = Intent(this, MainActivity::class.java )
            startActivity(intent)
        }
    }




}
