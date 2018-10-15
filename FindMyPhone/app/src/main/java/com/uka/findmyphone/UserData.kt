package com.uka.findmyphone

import android.content.Context
import android.content.SharedPreferences

class UserData {
    var context: Context? = null

    var sharedPref: SharedPreferences? = null

    constructor(context: Context) {
        this.context = context

        sharedPref = context.getSharedPreferences("userData", Context.MODE_PRIVATE)
    }

    fun savePhone(phoneNum: String) {
        val editor = sharedPref!!.edit()

        editor.putString("phoneNumber", phoneNum)

        editor.commit()
    }

    fun loadPhone(): String {
        return sharedPref!!.getString("phoneNumber", "empty")
    }

    companion object {
        var myTrackers: MutableMap<String, String> = HashMap()
    }

    fun saveData(){
        var listOfTrackers = ""
        for((key, value) in myTrackers){
            if(listOfTrackers.length == 0){
                listOfTrackers = key + "%" + value
            }else{
                listOfTrackers += key + "%" + value
            }
        }

        if(listOfTrackers.length == 0){
            listOfTrackers = "empty"
        }

        val editor = sharedPref!!.edit()

        editor.putString("myTrackers", listOfTrackers)

        editor.commit()
    }

    fun loadData(){
        myTrackers.clear()

        val listOfTrackers = sharedPref!!.getString("myTrackers", "empty")

        if(!listOfTrackers.equals("empty")){
            val userInfo = listOfTrackers.split("%").toTypedArray()
            var i = 0
            while(i < userInfo.size){
                myTrackers.put(userInfo[i], userInfo[i+1])
                i+=2
            }
        }
    }
}