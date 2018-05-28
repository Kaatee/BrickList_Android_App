package com.example.kasia.bricklist

import android.media.Image

/**
 * Created by Kasia on 27.05.2018.
 */
class Brick {
    var code: String? = null
    var name: String?=null
    var image: Image?=null

    constructor(code: String, name: String, image: Image){
        this.code=code
        this.name=name
        this.image=image
    }
}