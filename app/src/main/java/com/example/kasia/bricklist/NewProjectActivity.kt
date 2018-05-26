package com.example.kasia.bricklist

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_new_project.*

class NewProjectActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_project)

        settingsButton.setOnClickListener(){
            startActivitySettings()
        }
    }


    public fun startActivitySettings(){
        val intent = Intent(this, SettingsActivity::class.java )
        startActivity(intent)
    }
}
