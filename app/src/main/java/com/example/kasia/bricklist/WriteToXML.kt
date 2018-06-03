package com.example.kasia.bricklist

import android.content.Context
import android.support.v4.app.ActivityCompat.requestPermissions
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
import android.Manifest
import android.content.pm.PackageManager
import android.support.v4.content.ContextCompat


import android.icu.util.Output
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.ListView

//import kotlinx.android.synthetic.main.activity_set.*
import java.util.*


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


/*
fun export( v: View){


        val docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
        val doc = docBuilder.newDocument()

        val rootElement : Element= doc.createElement("person")

        rootElement.setAttribute("person-id", "1001")

        val lastName :Element = doc.createElement("last-name")

        lastName.appendChild(doc.createTextNode("Doe"))
        rootElement.appendChild(lastName)

        val firstName : Element = doc.createElement("first-name")
        firstName.appendChild(doc.createTextNode("John"))
        rootElement.appendChild(firstName)
        doc.appendChild(rootElement)


        val tranformer : Transformer = TransformerFactory.newInstance().newTransformer()
        tranformer.setOutputProperty(OutputKeys.INDENT,"yes")
        tranformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount" , "2")
        val path =this.filesDir
        val outDir = File(path,"Output")
        outDir.mkdir()

        val file = File (outDir,"text.xml")

        tranformer.transform(DOMSource(doc) , StreamResult(file))





    }
 */

}