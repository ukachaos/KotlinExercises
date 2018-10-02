package com.uka.twitterdemo

class PostInfo {
    var uid: String? = null
    var text: String? = null
    var imageDownloadURL: String? = null

    constructor(uid: String, text: String, imageDownloadURL: String) {
        this.uid = uid
        this.text = text
        this.imageDownloadURL = imageDownloadURL
    }
}