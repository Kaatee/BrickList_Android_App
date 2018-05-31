package com.example.kasia.bricklist

/**
 * Created by Kasia on 30.05.2018.
 */
class Inventory {
    var id: Int? = null
    var name: String? = null
    var active: Int? = null
    var lastAccessed: Int? = null
    var parts:ArrayList<InventoriesPart>? = null

    constructor(id:Int?, name:String?, active:Int?, lastAccessed:Int?){
        this.id = id
        this.name = name
        this.active = active
        this.lastAccessed = lastAccessed
    }

    constructor(){

    }


}