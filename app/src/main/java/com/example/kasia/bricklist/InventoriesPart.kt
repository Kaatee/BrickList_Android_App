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
    var alternate: String? = null
    var id:Int? = null
    var inventoryID:Int? = null
    var typeID:Int? = null
    var quantityInSet:Int? = null
    var quantityInStore:Int = 0
    var colorID:Int? = null

    fun print(brick: InventoriesPart){
        var str = "------ itemType: "+brick.itemType + "itemID: " +  brick.itemId+ "gty: " + brick.qty + "color: "+ brick.color+"extra: " +
                brick.extra+ "alternate: " + brick.alternate+ "id: "+ brick.id+ "InventoryID: "+ brick.inventoryID + "typeID: "+
                brick.typeID + "quantityInSet: " +brick.quantityInSet +"quantityInStore: " +brick.quantityInStore+ "colorID: "+brick.colorID
        Log.i(str, str )

    }


}