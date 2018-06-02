package com.example.kasia.bricklist

import android.content.Context
import android.os.Environment
import android.widget.Toast
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

/**
 * Created by Kasia on 02.06.2018.
 */
class WriteToXML {
   var items: ArrayList<InventoriesPart>?= null
    var context:Context

    constructor(items: ArrayList<InventoriesPart>, contex: Context){
        this.items = items
        this.context = contex
    }

    fun writeXML(items: ArrayList<InventoriesPart>){
        val docBuilder: DocumentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
        val doc: Document = docBuilder.newDocument()

        val rootElement: Element = doc.createElement("INVENTORY")

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

            Toast.makeText(context,"Plik został zapisany na karte SD",Toast.LENGTH_SHORT).show()
        }
    }


}