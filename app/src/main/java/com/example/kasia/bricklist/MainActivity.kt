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
            ///TODO
           /// writeXML(inventoryName)
        }
    }

    public fun startActivityNewProject(){
        val intent = Intent(this, NewProjectActivity::class.java )
        startActivity(intent)
    }

   fun writeXML(inventoryName: String){
        val docBuilder: DocumentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
        val doc: Document = docBuilder.newDocument()

        val rootElement: Element = doc.createElement("INVENTORY")
        var items: ArrayList<InventoriesPart>
       // downloading InventoryParts from database
        val database = DataBaseHelper(this)
        items = database.getInventoriesParts(inventoryName)
        database.close()

        for(inventoryPart in items){ //ArrayList<InventoryParts>
            if(inventoryPart.quantityInStore!=inventoryPart.quantityInSet){
                val rootItem = doc.createElement("ITEM")
                val itemType = doc.createElement("ITEMTYPE")
                itemType.appendChild(doc.createTextNode(inventoryPart.itemType))
                rootItem.appendChild(itemType)
                val itemId = doc.createElement("ITEMID")
                itemId.appendChild(doc.createTextNode(inventoryPart.itemId))
                rootItem.appendChild(itemId)
                val color = doc.createElement("COLOR")
                color.appendChild(doc.createTextNode(inventoryPart.color.toString()))
                rootItem.appendChild(color)
                val qty = doc.createElement("QTYFILLED")
                qty.appendChild(doc.createTextNode((inventoryPart.quantityInSet - inventoryPart.quantityInStore).toString()))
                rootItem.appendChild(qty)

                rootElement.appendChild(rootItem)
            }

            doc.appendChild(rootElement)
            val transformer : Transformer = TransformerFactory.newInstance().newTransformer()

            transformer.setOutputProperty(OutputKeys.INDENT,"yes")
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2")

            var path: File = Environment.getExternalStorageDirectory()
            val outDir = File(path, "BrickListFiles")
            outDir.mkdir()

            val file = File(path.toString() + "/BrickListFiles/brickNeeded.xml")
            transformer.transform(DOMSource(doc), StreamResult(file))

            Toast.makeText(this,"Plik został zapisany na karte SD",Toast.LENGTH_SHORT).show()
        }
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
