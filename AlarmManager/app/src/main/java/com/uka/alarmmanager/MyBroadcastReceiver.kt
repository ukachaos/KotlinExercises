package com.uka.alarmmanager

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class MyBroadcastReceiver : BroadcastReceiver(){
    override fun onReceive(context: Context?, intent: Intent?) {
        if(intent!!.action.equals("com.uka.alarmmanager")){
            var b= intent.extras
            Toast.makeText(context, b.getString("message"), Toast.LENGTH_LONG).show();
        }else if(intent!!.action.equals("android.intent.action.BOOT_COMPLETED")){
            val saveData = SaveData(context!!)
            saveData.setAlarm(saveData.getHour(), saveData.getMinute())
        }
    }

}