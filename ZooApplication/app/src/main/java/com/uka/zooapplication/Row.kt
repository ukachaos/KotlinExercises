package com.uka.zooapplication

class Row{
    var name:String?=null
    var des:String?=null
    var image:Int?=null
    var isKiller:Boolean?=null
    constructor(name:String, des:String, image:Int, isKiller:Boolean){
        this.name = name
        this.des = des
        this.image = image
        this.isKiller = isKiller
    }
}