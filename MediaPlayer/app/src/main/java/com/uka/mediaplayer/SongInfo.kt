package com.uka.mediaplayer

class SongInfo{
    var title:String?=null
    var author:String?=null
    var songURL:String?=null

    constructor(title:String, author:String, songURL:String){
        this.title = title
        this.author = author
        this.songURL = songURL
    }
}