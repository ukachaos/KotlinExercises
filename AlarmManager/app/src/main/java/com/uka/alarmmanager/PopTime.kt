package com.uka.alarmmanager

import android.os.Build
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TimePicker

class PopTime : DialogFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var myView = inflater.inflate(R.layout.pop_time, container, false)

        var mButtonDone = myView.findViewById(R.id.mButtonDone) as Button

        var mTimePicker = myView.findViewById(R.id.tp1) as TimePicker

        mButtonDone.setOnClickListener {
            val mainActivity = activity as MainActivity
            if (Build.VERSION.SDK_INT > 23) {
                mainActivity.setTime(mTimePicker.hour, mTimePicker.minute)
            } else {
                mainActivity.setTime(mTimePicker.currentHour, mTimePicker.currentMinute)
            }

            this.dismiss()
        }

        return myView
    }
}