package com.uka.alarmmanager

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onClickSettime(view: View) {
        val popTime = PopTime()
        val fragmentManager = supportFragmentManager
        popTime.show(fragmentManager, "Select time")
    }

    fun setTime(hour: Int, minute: Int) {
        mTextTime.text = hour.toString() + " : " + minute.toString()

        val saveData = SaveData(this)
        saveData.setAlarm(hour, minute)
    }
}
