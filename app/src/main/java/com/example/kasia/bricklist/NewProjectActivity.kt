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
import java.util.*
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

    fun loadData(inventoryID: Int, inventoryName: String){

        val filename = "downloadedXML.xml"
        val path = filesDir
        val inDir = File(path, "XML")

        if(inDir.exists()){
            val file = File(inDir, filename)
            if(file.exists()){
                val xmlDoc : Document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file)

                xmlDoc.documentElement.normalize()

               //val database =MyDBHandler(this, null, null, 1)
                val database = DataBaseHelper(this)

                val inventory = Inventory(inventoryID, inventoryName,1, Date().time.toInt())
                database.addInventory(inventory)

                val items : NodeList = xmlDoc.getElementsByTagName("ITEM")
                val brick = InventoriesPart()
                for(i in 0..items.length-1){
                    val itemNode: Node = items.item(i)
                    if(itemNode.getNodeType() == Node.ELEMENT_NODE){
                        val elem = itemNode as Element
                        val children = elem.childNodes

                        for (j in 0..children.length-1){
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

                        if(brick.alternate.equals("N") ){ //nie dodajemy alternate o innej wart niz  N
                            brick.typeID = database.getTypeID(brick)
                            brick.quantityInStore = 0
                            brick.quantityInSet = brick.qty!!
                            brick.colorID = database.getColorID(brick)
                            brick.id = database.generateID()
                            brick.inventoryID =inventoryID
                            brick.alternate = "N"
                            brick.itemID_DB = database.getItemID_DB(brick)

                            //handling image
                            //var url:String =  "https://www.lego.com/service/bricks/5/2/" + brick.designID
                            //if(!getImage(url, ))

                        }
                    }
                    database.addBrick(brick)
                    database.close()
                }
            }
        }
    }

    private inner class XmlDownloader: AsyncTask<String, Int, String>(){

        override fun doInBackground(vararg params: String?): String {
            try {
                var filename = "downloadedXML.xml"

                val url = URL(params[0])
                val connection = url.openConnection()
                connection.connect()
                val isStream = url.openStream()
                val testDirectory = File("$filesDir/XML")
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

            var idT: String? = params[0]?.split("/")?.get(5)
            var idT2: String? = idT?.split(".")?.get(0)
            var x: Int = idT2!!.toInt()

            val projectNameEditText: EditText = findViewById<EditText>(R.id.enterNameText)
            var projectName: String = projectNameEditText.text.toString()
            loadData(x,projectName)
            return "success"
        }
    }

    fun downloadData(path:String){
        val cd = XmlDownloader()
        cd.execute(path)
    }


}

