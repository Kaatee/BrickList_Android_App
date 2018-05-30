package com.example.kasia.bricklist

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.io.FileOutputStream
import java.io.IOException
import java.sql.SQLException


/**
 * Created by Kasia on 30.05.2018.
 */
class DataBaseHelper
/**
 * Constructor
 * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
 * @param context
 */
(private val myContext: Context) : SQLiteOpenHelper(myContext, DB_NAME, null, 1) {

    private var myDataBase: SQLiteDatabase? = null

    /**
     * Creates a empty database on the system and rewrites it with your own database.
     */
    @Throws(IOException::class)
    fun createDataBase() {

        val dbExist = checkDataBase()

        if (dbExist) {
            //do nothing - database already exist
        } else {

            //By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
            this.readableDatabase

            try {

                copyDataBase()

            } catch (e: IOException) {

                throw Error("Error copying database")

            }

        }

    }

    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     */
    private fun checkDataBase(): Boolean {

        var checkDB: SQLiteDatabase? = null

        try {
            val myPath = DB_PATH + DB_NAME
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY)

        } catch (e: SQLiteException) {

            //database does't exist yet.

        }

        if (checkDB != null) {

            checkDB.close()

        }

        return if (checkDB != null) true else false
    }

    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     */
    @Throws(IOException::class)
    private fun copyDataBase() {

        //Open your local db as the input stream
        val myInput = myContext.getAssets().open(DB_NAME)

        // Path to the just created empty db
        val outFileName = DB_PATH + DB_NAME

        //Open the empty db as the output stream
        val myOutput = FileOutputStream(outFileName)

        //transfer bytes from the inputfile to the outputfile
        val buffer = ByteArray(1024)
        var length: Int = myInput.read(buffer)
        while (length  > 0) {
            myOutput.write(buffer, 0, length)
            length = myInput.read(buffer)
        }

        //Close the streams
        myOutput.flush()
        myOutput.close()
        myInput.close()

    }

    @Throws(SQLException::class)
    fun openDataBase() {

        //Open the database
        val myPath = DB_PATH + DB_NAME
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY)

    }

    @Synchronized override fun close() {

        if (myDataBase != null)
            myDataBase!!.close()

        super.close()

    }

    override fun onCreate(db: SQLiteDatabase) {

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

    }

    companion object {

        //The Android's default system path of your application database.
        private val DB_PATH = "/data/data/com.example.kasia.bricklist/databases/"

        private val DB_NAME = "xyz.db"
    }

    // Add your public helper methods to access and get content from the database.
    // You could return cursors by doing "return myDataBase.query(....)" so it'd be easy
    // to you to create adapters for your views.
    fun getTypeID(x: InventoriesPart):Int{
        val query = "SELECT id FROM ItemTypes WHERE code = '" +x.itemType +"'"
        val db = this.writableDatabase
        Log.i("----Jestem TUc","---Jestem TUc")
        try {
            val cursor = db.rawQuery(query, null)

            Log.i("----Jestem TUd","---Jestem TUd")
            var typeID = 0

            if(cursor.moveToFirst()){
                typeID = Integer.parseInt(cursor.getString(0))
            }
            cursor.close()
            db.close()
            return typeID
        }catch(e: Exception){ Log.i("----Blad: "+e.message,"---Blad: "+e.message)}
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