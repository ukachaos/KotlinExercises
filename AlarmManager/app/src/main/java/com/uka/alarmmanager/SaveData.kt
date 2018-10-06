package com.uka.alarmmanager

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import java.util.*

class SaveData{

    var context:Context?=null

    var sharedRef:SharedPreferences

    constructor(context:Context){
        this.context = context
        sharedRef = context.getSharedPreferences("myRef", Context.MODE_PRIVATE)
    }

    fun saveAlarm(hour:Int, minute:Int){
        var editor = sharedRef.edit()
        editor.putInt("hour", hour)
        editor.putInt("minute", minute)

        editor.commit()
    }

    fun getHour():Int{
        return sharedRef!!.getInt("hour", 0)
    }

    fun getMinute():Int{
        return sharedRef!!.getInt("minute", 0)
    }

    fun setAlarm(hour:Int, minute:Int){
        saveAlarm(hour, minute)
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, 0)

        val alarmManager = context!!.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        var intent = Intent(context, MyBroadcastReceiver::class.java)
        intent.putExtra("message"," alarm time")
        intent.action = "com.uka.alarmmanager"
        val pi = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_DAY, pi)
    }
}