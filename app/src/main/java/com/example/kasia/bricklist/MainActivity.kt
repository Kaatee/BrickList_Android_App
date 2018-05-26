package com.example.kasia.bricklist

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        newProjectButton.setOnClickListener(){
            startActivityNewProject()
        }
    }

    public fun startActivityNewProject(){
        val intent = Intent(this, NewProjectActivity::class.java )
        startActivity(intent)
    }
}
