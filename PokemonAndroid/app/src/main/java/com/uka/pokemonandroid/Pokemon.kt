package com.uka.pokemonandroid

import android.location.Location

class Pokemon{
    var name:String?=null
    var des:String?=null
    var image:Int?=null
    var power:Double?=null
    var location:Location?=null
    var isCatch:Boolean?=false

    constructor(name:String, des:String, image:Int, power:Double, lat:Double, lon:Double){
        this.name = name
        this.des = des
        this.image = image
        this.power = power
        this.location = Location(name)
        location!!.longitude = lon
        location!!.latitude = lat
    }
}