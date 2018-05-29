package com.example.kasia.bricklist

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_new_project.*
import java.io.*
import java.net.MalformedURLException
import java.net.URL

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
            val path = url + id+".xml"
            ////// save xml from url
            var itemID: String = itemIDEditText.text.toString()
            var projectName: String = projectNameEditText.text.toString()
            val filename = itemID+"__"+projectName+".xml"
            downloadData(path)
            Toast.makeText(this,"Projekt zostal utworzony",Toast.LENGTH_LONG).show()
            ///////

            var intent = Intent(this, MainActivity::class.java )
            startActivity(intent)


        }
    }

    private inner class XmlDownloader: AsyncTask<String, Int, String>(){

        override fun doInBackground(vararg params: String?): String { //params - full url
            try {
                val itemIDEditText: EditText = findViewById<EditText>(R.id.enterItemIdText)
                val projectNameEditText: EditText = findViewById<EditText>(R.id.enterNameText)
                var filename = itemIDEditText.text.toString()+"__"+projectNameEditText.text.toString()+".xml"

                val url = URL(params[0])
                val connection = url.openConnection()
                connection.connect()
               // val lengthOfFile = connection.contentLength
                val isStream = url.openStream()
                val testDirectory = File("$filesDir/XML")
                Log.i("----- XXXXX"+"$filesDir/XML","-----$filesDir/XML")
                if (!testDirectory.exists()) testDirectory.mkdir()
                val fos = FileOutputStream("$testDirectory/$filename")
                val data = ByteArray(1024)
                var count = 0
                var total : Long = 0
                count = isStream.read(data)

                while (count != -1) {
                    total+=count.toLong()
                    fos.write(data, 0, count)
                    count = isStream.read(data)
                }
                isStream.close()
                fos.close()
            }
            catch(e: MalformedURLException){
                Log.i("-----Malformed URL", e.toString())
                return "Malformed URL"
            }
            catch (e: FileNotFoundException){
                Log.i("-----File not found", e.toString())
                return "File not found"
            }
            catch (e: IOException){
                Log.i("-----IO Exception", e.toString())
                return "IO Exception"
            }

            return "success"
        }
    }

    fun downloadData(path:String){
        val cd = XmlDownloader()
        cd.execute(path)
    }

    fun refresh(v: View,path:String){
        downloadData(path)
    }

}

