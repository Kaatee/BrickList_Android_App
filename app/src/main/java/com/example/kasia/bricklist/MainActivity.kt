package com.example.kasia.bricklist

import android.content.ContextWrapper
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.*
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.FileOutputStream
import android.widget.AdapterView
import org.w3c.dom.Document
import org.w3c.dom.Element
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.OutputKeys
import javax.xml.transform.Transformer
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult


class MainActivity : AppCompatActivity() {

    var database: DataBaseHelper? = null
    var inventoriesNames: ArrayList<String?> = java.util.ArrayList()
    var inventories : ArrayList<Inventory>? =null
    //var adapter: ArrayAdapter<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        copyDB()
        database = DataBaseHelper(this)

       // var inventories: ArrayList<Inventory>
        inventories = database?.getInventories()

        for(i in 0..inventories!!.size-1){
            inventoriesNames.add(inventories!!.get(i).name)
        }

        var adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, inventoriesNames)
        listView.adapter = adapter

        listView.onItemClickListener = AdapterView.OnItemClickListener{
            adapterView, view, position, id ->
            val intent = Intent(this, LegoSetActivity::class.java )
            intent.putExtra("name",inventoriesNames[position] )
            startActivity(intent)
        }

        newProjectButton.setOnClickListener(){
            startActivityNewProject()
        }

        archiveButton.setOnClickListener(){
            ///TODO inventory name
           /// writeXML(inventoryName)
        }
    }

    public fun startActivityNewProject(){
        val intent = Intent(this, NewProjectActivity::class.java )
        startActivity(intent)
    }



    override fun onRestart() {
        super.onRestart()
        inventories = database?.getInventories()
        inventoriesNames.clear()
        inventories!!.forEach {
            inventoriesNames.add(it.name!!)
        }
        var adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1,inventoriesNames)
        listView.adapter = adapter
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
