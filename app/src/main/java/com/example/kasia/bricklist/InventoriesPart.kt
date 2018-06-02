package com.example.kasia.bricklist

import android.media.Image
import android.util.Log

/**
 * Created by Kasia on 27.05.2018.
 */
class InventoriesPart {
    var itemType: String? = null
    var itemId: String?=null
    var qty: Int?=null
    var color:Int? =null
    var extra: String? = null
    var alternate : String? = null

    var id:Int? = null
    var inventoryID:Int? = null
    var typeID:Int? = null
    var itemID_DB: Int = 0
    var quantityInSet:Int = 0
    var quantityInStore:Int = 0
    var colorID:Int? = null
    var name:String? = null

    var image: ByteArray? = null
    var designID: Int? =null

}