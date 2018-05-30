package com.example.kasia.bricklist

import android.content.Intent
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_new_project.*
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import java.io.*
import java.net.MalformedURLException
import java.net.URL
import javax.xml.parsers.DocumentBuilderFactory

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

    fun loadData(inventoryID: Int){

        val filename = "downloadedXML.xml"
        val path = filesDir
        val inDir = File(path, "XML")

        if(inDir.exists()){
            Log.i("---Jestem TU","---Jestem TU")
            val file = File(inDir, filename)
            if(file.exists()){
                val xmlDoc : Document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file)

                xmlDoc.documentElement.normalize()

               //val database =MyDBHandler(this, null, null, 1)
                val database = DataBaseHelper(this)
                ///try{database.openDataBase()} catch(e:Exception){
                 ///   Log.i("---Blad: "+e.message,"---Blad: "+e.message )
               /// }

                val items : NodeList = xmlDoc.getElementsByTagName("ITEM")
                val brick = InventoriesPart()
                Log.i("----Jestem TU2","---Jestem TU2")
                for(i in 0..items.length-1){
                    Log.i("----Jestem TU2.5","---Jestem TU2.5")
                    val itemNode: Node = items.item(i)
                    if(itemNode.getNodeType() == Node.ELEMENT_NODE){
                        Log.i("----Jestem TU3","---Jestem TU3")
                        val elem = itemNode as Element
                        val children = elem.childNodes


                         //nie dodajemy alternate o innej wart niz  N

                        for (j in 0..children.length-1){
                            Log.i("---Jestem TU4","---Jestem TU4")
                            val node = children.item(j)
                            if(node is Element){
                                when(node.nodeName){
                                    "ITEMTYPE" -> brick.itemType = node.textContent
                                    "ITEMID" -> brick.itemId = node.textContent
                                    "QTY" -> brick.qty = node.textContent.toInt()
                                    "COLOR" -> brick.color = node.textContent.toInt()
                                    "EXTRA" -> brick.extra = node.textContent
                                    "ALTERNATE" -> brick.alternate = node.textContent

                                }
                            }
                        }

                        if(brick.alternate.equals("N") ){
                            Log.i("---Jestem TU5.0","---Jestem TU5.0")
                            //try {
                                brick.typeID = database.getTypeID(brick)
                            //} catch(e:Exception){Log.i("---"+e.message, "---"+e.message)}
                           // Log.i("---Jestem TU5.1","---Jestem TU5.1")
                            brick.quantityInStore = 0
                            Log.i("---Jestem TU5.2","---Jestem TU5.2")
                            brick.quantityInSet = database.getQuantityInSet(brick)
                            brick.colorID = database.getColorID(brick)
                            brick.id = database.generateID()
                            brick.inventoryID =inventoryID
                            Log.i("---Jestem TU5","---Jestem TU5")
                        }
                    }
                    Log.i("---Jestem TU6","---Jestem TU6")
                    database.addBrick(brick)
                    Log.i("---Dodalem do databasy"+brick.print(brick),"---Dodalem do databasy"+brick.print(brick))
                    database.close()
                }

            }
        }
    }

    private inner class XmlDownloader: AsyncTask<String, Int, String>(){

        override fun doInBackground(vararg params: String?): String { //params - full url
            try {
                var filename = "downloadedXML.xml"

                val url = URL(params[0])
                val connection = url.openConnection()
                connection.connect()
               // val lengthOfFile = connection.contentLength
                val isStream = url.openStream()
                val testDirectory = File("$filesDir/XML")
                //Log.i("----- XXXXX"+"$filesDir/XML","-----$filesDir/XML")
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

//http://fcds.cs.put.poznan.pl/MyWeb/BL/615"
            var idT: String? = params[0]?.split("/")?.get(5)
            var idT2: String? = idT?.split(".")?.get(0)
            var x: Int = idT2!!.toInt()
            loadData(x)
            return "success"
        }
    }

    fun downloadData(path:String){
        val cd = XmlDownloader()
        cd.execute(path)
    }


}

