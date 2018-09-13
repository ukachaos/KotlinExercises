package com.uka.noteapp

import android.content.ContentValues
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_notes.*

class AddNotes : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_notes)
    }

    fun onButtonAdd(view:View){

        var values = ContentValues()
        values.put("title", tvTitle.text.toString())
        values.put("description", tvDes.text.toString())

        var dbManager = DBManager(this)
        val ID = dbManager.insert(values)

        if(ID>0){
            Toast.makeText(this, "Note saved", Toast.LENGTH_LONG).show()
        }
        else{
            Toast.makeText(this, "Something went wrong!", Toast.LENGTH_LONG).show()
        }

        finish()
    }
}
