package com.example.kasia.bricklist

import android.content.ContextWrapper
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.FileOutputStream
import android.widget.AdapterView



class MainActivity : AppCompatActivity() {
    var database: DataBaseHelper? = null
    var inventoriesNames: ArrayList<String?> = java.util.ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        copyDB()
        database = DataBaseHelper(this)
        var inventories: ArrayList<Inventory>
        inventories = database!!.getInventories()
        Log.i("--- Tu jestem 5", "--- Tu jestem 5: rozmiar :"+inventories.size.toString())

        for(i in 0..inventories.size-1){
            inventoriesNames.add(inventories.get(i).name)
        }

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, inventoriesNames)
        listView.adapter = adapter

        //listView.setOnItemClickListener() //klikniecie elementu
 ////////////////
        listView.onItemClickListener = AdapterView.OnItemClickListener{
            adapterView, view, position, id ->
            val intent = Intent(this, LegoSetActivity::class.java )
            intent.putExtra("name",inventoriesNames[position] )
            startActivity(intent)
        }
        /////////////

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
