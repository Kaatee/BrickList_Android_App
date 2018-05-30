package com.example.kasia.bricklist

import android.content.ContextWrapper
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.FileOutputStream

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        copyDB()


        newProjectButton.setOnClickListener(){
            startActivityNewProject()
        }

        //tymczasowo podpieta lista z adaptera
        archiveButton.setOnClickListener(){
            val intent = Intent(this, LegoSetActivity::class.java )
            startActivity(intent)
        }
    }

    public fun startActivityNewProject(){
        val intent = Intent(this, NewProjectActivity::class.java )
        startActivity(intent)
    }


    private fun copyDB() {

        val cw = ContextWrapper(applicationContext)
        val db_name = "xyz.db"
        val db_path = cw.dataDir.absolutePath
        val outDir = File(db_path, "databases")
        outDir.mkdir()
        val file = File(db_path + "/databases/", db_name)
        if(!file.exists()){
            val input =applicationContext.getAssets().open("xyz.db");
            val mOutput = FileOutputStream(file)
            val mBuffer = ByteArray(1024)
            var mLength = input.read(mBuffer)
            while (mLength > 0) {
                mOutput.write(mBuffer, 0, mLength)
                mLength = input.read(mBuffer)
            }
            mOutput.flush()
            mOutput.close()
            input.close()
        }

    }
}
