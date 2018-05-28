package com.example.kasia.bricklist

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val idEditText: EditText = findViewById<EditText>(R.id.idText)
        val urlEditText: EditText = findViewById<EditText>(R.id.urlText)

        var url: String
        var id: String

        //set url and ItemID when load
        try{
            url = getIntent().getStringExtra("url")
            id = getIntent().getStringExtra("id")
            idEditText.setText(id)
            urlEditText.setText(url)
        }catch(e:Exception){}

        //receive from newProjectActivity
        val projectName : String = getIntent().getStringExtra("projectName")
        val itemID : String = getIntent().getStringExtra("itemID")

        saveButton.setOnClickListener(){
            url = urlEditText.text.toString()
            id = idEditText.text.toString()

            val intent = Intent(this, NewProjectActivity::class.java )
            intent.putExtra("url", url)
            intent.putExtra("id",id)
            intent.putExtra("projectName", projectName)
            intent.putExtra("itemID",itemID)
            startActivity(intent)

        }
    }
}
