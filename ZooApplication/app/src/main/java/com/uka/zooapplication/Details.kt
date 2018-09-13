package com.uka.zooapplication

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_details.*

class Details : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        val b:Bundle = intent.extras

        val name = b.getString("name")
        val des = b.getString("des")
        val image = b.getInt("image")

        tvName.setText(name)
        tvDes.setText(des)
        ivAnimalImage.setImageResource(image)
    }
}
