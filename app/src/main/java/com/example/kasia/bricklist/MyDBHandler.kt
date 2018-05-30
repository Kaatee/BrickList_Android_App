package com.example.kasia.bricklist

import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.Context
import android.content.ContentValues
import android.util.Log

/**
 * Created by Kasia on 27.05.2018.
 */
class MyDBHandler(context: Context, name: String?, factory: SQLiteDatabase.CursorFactory?, version: Int) : SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {

    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "xyz.db"
    }

    override fun onCreate(db: SQLiteDatabase?) {
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }

//    fun getInventoryID(x: InventoriesPart):Int{
//        val query = "SELECT InventoryID FROM InventoriesParts WHERE "
//
//    } INVENTORY ID = TO CO PO URL

    fun getTypeID(x: InventoriesPart):Int{
        val query = "SELECT _id FROM ItemTypes WHERE code = '" +x.itemType +"'"
        val db = this.writableDatabase
        Log.i("---Jestem TUc","---Jestem TUc")
        try {
            val cursor = db.rawQuery(query, null)

        Log.i("---Jestem TUd","---Jestem TUd")
        var typeID = 0

        if(cursor.moveToFirst()){
            typeID = Integer.parseInt(cursor.getString(0))
        }
        Log.i("---Jestem TUf","---Jestem TUf")
        cursor.close()
        Log.i("---Jestem TUg","---Jestem TUg")
        db.close()
        Log.i("---Jestem TUh"+typeID,"---Jestem TUh"+typeID)
        return typeID
        }catch(e: Exception){ Log.i("---Blad: "+e.message,"---Blad: "+e.message)}
        return 0
    }

    fun getQuantityInSet(x: InventoriesPart):Int{
        val query = "SELECT quantityInSet FROM InventoriesParts WHERE id = '" +x.id +"'"
        val db = this.writableDatabase
        val cursor = db.rawQuery(query, null)
        var quantityInSet: Int = 0

        if(cursor.moveToFirst()){
            quantityInSet = Integer.parseInt(cursor.getString(0))
        }
        cursor.close()
        db.close()
        return quantityInSet
    }

    fun getQuantityInStore(x: InventoriesPart):Int{
        val query = "SELECT quantityInStore FROM InventoriesParts WHERE id = '" +x.id +"'"
        val db = this.writableDatabase
        val cursor = db.rawQuery(query, null)
        var quantityInStore: Int = 0

        if(cursor.moveToFirst()){
            quantityInStore = Integer.parseInt(cursor.getString(0))
        }
        cursor.close()
        db.close()
        return quantityInStore
    }

    fun getColorID(x: InventoriesPart):Int{
        val query = "SELECT id FROM Colors WHERE Code = '" +x.color +"'"
        val db = this.writableDatabase
        val cursor = db.rawQuery(query, null)
        var colorID: Int = 0

        if(cursor.moveToFirst()){
            colorID = Integer.parseInt(cursor.getString(0))
        }
        cursor.close()
        db.close()
        return colorID
    }

    fun generateID():Int{
        val query = "Select COUNT(id) AS x FROM InventoriesParts"
        val db = this.writableDatabase
        val cursor = db.rawQuery(query, null)
        var newID: Int = 0

        if(cursor.moveToFirst()){
            newID = Integer.parseInt(cursor.getString(0))
        }
        cursor.close()
        db.close()
        return newID
    }
/*

                                    "ITEMID" -> brick.itemId = node.textContent
                                    "QTY" -> brick.qty = node.textContent.toInt()
                                    "COLOR" -> brick.color = node.textContent.toInt()
                                    "EXTRA" -> brick.extra = node.textContent
                                    "ALTERNATE" -> brick.alternate = node.textContent
                            brick.typeID = database.getTypeID(brick)
                            brick.quantityInStore = 0
                            brick.quantityInSet = database.getQuantityInSet(brick)
                            brick.colorID = database.getColorID(brick)
                            brick.id = database.generateID()
                            brick.inventoryID =inventoryID
 */
    fun addBrick(brick: InventoriesPart){
        val values = ContentValues()
        values.put("TypeID",brick.typeID)
        values.put("QuantityInSet",brick.quantityInSet)
        values.put("QuantityInStore",brick.quantityInStore)
        values.put("ColorID",brick.colorID)
        values.put("ItemID",brick.itemId)
        values.put("InventoryID",brick.inventoryID)

        val db = this.writableDatabase
        db.insert("InventoriesParts", null, values)
        db.close()
    }
}