package com.example.kasia.bricklist

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_new_project.*

class NewProjectActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_project)

        val itemIDEditText: EditText = findViewById<EditText>(R.id.enterItemIdText)
        val projectNameEditText: EditText = findViewById<EditText>(R.id.enterNameText)

        var url = "http://fcds.cs.put.poznan.pl/MyWeb/BL/";
        var id = "615";

        //set projectName and ItemID whe come back from settings
        try {
            url = getIntent().getStringExtra("url")
            id= getIntent().getStringExtra("id")

            var projectNametxt: String = getIntent().getStringExtra("projectName")
            var itemIdtxt: String = getIntent().getStringExtra("itemID")

            itemIDEditText.setText(itemIdtxt)
            projectNameEditText.setText(projectNametxt)
        }catch(e: Exception){ }


        settingsButton.setOnClickListener(){
            var itemID: String = itemIDEditText.text.toString()
            var projectName: String = projectNameEditText.text.toString()
            var intent = Intent(this, SettingsActivity::class.java )
            intent.putExtra("itemID", itemID)
            intent.putExtra("projectName", projectName)
            intent.putExtra("url", url)
            intent.putExtra("id", id)
            startActivity(intent)
        }

        createButton.setOnClickListener(){
            val path = url + id
            // TO DO: Create new project
            var intent = Intent(this, MainActivity::class.java )
            startActivity(intent)
        }
    }
}
