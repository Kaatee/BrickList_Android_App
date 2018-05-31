package com.example.kasia.bricklist

import android.content.ContextWrapper
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.FileOutputStream

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        copyDB()
        Log.i("--- Tu jestem 1", "--- Tu jestem 1")
        val database = DataBaseHelper(this)
        Log.i("--- Tu jestem 2", "--- Tu jestem 2")
        var inventories: ArrayList<Inventory>
        Log.i("--- Tu jestem 3", "--- Tu jestem 3")
        inventories = database.getInventories()
        Log.i("--- Tu jestem 4", "--- Tu jestem 4")
        val inventoriesNames : ArrayList<String?> = java.util.ArrayList()
        Log.i("--- Tu jestem 5", "--- Tu jestem 5"+inventories.size.toString())

        for(i in 0..inventories.size-1){
            Log.i("--- jestem w for", "--- jestem w for")
            inventoriesNames.add(inventories.get(i).name)
        }

        Log.i("--- Tu jestem 6", "--- Tu jestem 6")
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, inventoriesNames)
        Log.i("--- Tu jestem 7", "--- Tu jestem 7")
        listView.adapter = adapter

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
