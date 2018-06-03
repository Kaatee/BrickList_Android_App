package com.example.kasia.bricklist

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_lego_set.*
import org.w3c.dom.Document
import org.w3c.dom.Element
import java.io.File
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.OutputKeys
import javax.xml.transform.Transformer
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult

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
            var items: ArrayList<InventoriesPart> = database.getInventoriesParts(inventoryName)
            database.close()
            //var x = WriteToXML(items, this)

            requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),1)
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                writeXML(items)
            }

//            val PERMISSIONS = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
//            ActivityCompat.requestPermissions(this, PERMISSIONS, 1)
//
//            try {
//                if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
//                    Log.i("---", "IF")
//                    x.writeXML(items)
//                }
//                Log.i("---","Zapisalem XML")
//            } catch(e:Exception){Log.i("---", "Blad: "+e.message )}

           // val intent = Intent(this, MainActivity::class.java )
           // startActivity(intent)
        }
    }


    fun writeXML(items: java.util.ArrayList<InventoriesPart>){
        val docBuilder: DocumentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
        val doc: Document = docBuilder.newDocument()
        val rootElement: Element = doc.createElement("INVENTORY")
        for(inventoryPart in items) {
            if (inventoryPart.quantityInStore != inventoryPart.quantityInSet) {
                val rootItem = doc.createElement("ITEM")

                val itemType = doc.createElement("ITEMTYPE")
                itemType.appendChild(doc.createTextNode(inventoryPart.itemType))
                rootItem.appendChild(itemType)

                val itemId = doc.createElement("ITEMID")
                itemId.appendChild(doc.createTextNode(inventoryPart.itemId.toString()))
                rootItem.appendChild(itemId)

                val color = doc.createElement("COLOR")
                color.appendChild(doc.createTextNode(inventoryPart.color.toString()))
                rootItem.appendChild(color)

                val qty = doc.createElement("QTY")
                qty.appendChild(doc.createTextNode((inventoryPart.quantityInSet - inventoryPart.quantityInStore).toString()))
                rootItem.appendChild(qty)

                val extra = doc.createElement("EXTRA")
                extra.appendChild(doc.createTextNode((inventoryPart.extra).toString()))
                rootItem.appendChild(extra)

                rootElement.appendChild(rootItem)
            }
        }
        doc.appendChild(rootElement)
        val transformer : Transformer = TransformerFactory.newInstance().newTransformer()

        transformer.setOutputProperty(OutputKeys.INDENT,"yes")
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2")

        // val PERMISSIONS = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        //requestPermissions( this, PERMISSIONS,1)

        var path: File = Environment.getExternalStorageDirectory()
        val outDir = File(path, "BrickListFiles")
        outDir.mkdir()

        val file = File(path.toString() + "/BrickListFiles/brickNeeded.xml")
        transformer.transform(DOMSource(doc), StreamResult(file))
       Toast.makeText(this, "Plik został zapisany na karte SD", Toast.LENGTH_SHORT).show()

        val intent = Intent(this, MainActivity::class.java )
        startActivity(intent)
    }



}
