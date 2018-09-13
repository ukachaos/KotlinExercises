package com.uka.calculator

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }


    var op = "*"
    var oldNumber = ""

    var isNewOp = true

    fun onButtonEqualClick(view: View) {
        val newNumber = editEntry.text.toString()
        var finalNumber: Double? = null
        when (op) {
            "*" -> {
                finalNumber = oldNumber.toDouble() * newNumber.toDouble()
            }
            "/" -> {
                finalNumber = oldNumber.toDouble() / newNumber.toDouble()
            }
            "+" -> {
                finalNumber = oldNumber.toDouble() + newNumber.toDouble()
            }
            "-" -> {
                finalNumber = oldNumber.toDouble() - newNumber.toDouble()
            }
        }

        editEntry.setText(finalNumber.toString())
        isNewOp = true
    }

    fun onButtonOperationClick(view: View) {
        val buSelected = view as Button

        when (buSelected.id) {
            buMul.id -> {
                op = "*"
            }
            buAdd.id -> {
                op = "+"
            }
            buDiv.id -> {
                op = "/"
            }
            buSub.id -> {
                op = "-"
            }
        }

        oldNumber = editEntry.text.toString()
        isNewOp = true
    }

    fun onButtonNumberClick(view: View) {

        if (isNewOp)
            editEntry.setText("")
        isNewOp = false

        val buSelected = view as Button
        var number: String = editEntry.text.toString()
        when (buSelected.id) {
            bu0.id -> {
                number += "0"
            }
            bu1.id -> {
                number += "1"
            }
            bu2.id -> {
                number += "2"
            }
            bu3.id -> {
                number += "3"
            }
            bu4.id -> {
                number += "4"
            }
            bu5.id -> {
                number += "5"
            }
            bu6.id -> {
                number += "6"
            }
            bu7.id -> {
                number += "7"
            }
            bu8.id -> {
                number += "8"
            }
            bu9.id -> {
                number += "9"
            }
            buDot.id -> {
                //TODO: prevent adding more than one dot
                number += "."
            }
            buSign.id -> {
                number += "-" + number
            }
        }

        editEntry.setText(number)
    }

    fun onButtonPercentClick(view: View) {
        var num = editEntry.text.toString().toDouble() / 100
        editEntry.setText(num.toString())

        isNewOp = true
    }

    fun onButtonACClick(view: View) {
        editEntry.setText("0")
        isNewOp = true
    }
}
