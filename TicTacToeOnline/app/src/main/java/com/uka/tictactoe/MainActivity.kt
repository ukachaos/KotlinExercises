package com.uka.tictactoe

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private var mFirebaseAnalytics:FirebaseAnalytics?=null

    private var mDatabase = FirebaseDatabase.getInstance()

    private var myRef = mDatabase.reference

    var mEmail:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)

        mEmail = intent.extras.getString("email")
    }

    fun buClick(view: View) {
        val buSelected = view as Button

        var cellId = 0

        when (buSelected.id) {
            R.id.bu1 -> cellId = 1
            R.id.bu2 -> cellId = 2
            R.id.bu3 -> cellId = 3
            R.id.bu4 -> cellId = 4
            R.id.bu5 -> cellId = 5
            R.id.bu6 -> cellId = 6
            R.id.bu7 -> cellId = 7
            R.id.bu8 -> cellId = 8
            R.id.bu9 -> cellId = 9
        }

        //Toast.makeText(this, "ID : $cellId", Toast.LENGTH_LONG).show()

        PlayGame(cellId, buSelected)
    }

    var Player1 = ArrayList<Int>()
    var Player2 = ArrayList<Int>()
    var activePlayer = 1

    fun PlayGame(cellID: Int, buSelected: Button) {

        if (activePlayer == 1) {
            buSelected.text = "X"
            buSelected.setBackgroundColor(Color.GREEN)
            Player1.add(cellID)
            activePlayer = 2

            autoPlay()
        } else {
            buSelected.text = "O"
            buSelected.setBackgroundColor(Color.BLUE)
            Player1.add(cellID)
            activePlayer = 1
        }

        buSelected.isEnabled = false

        CheckWinner()
    }

    fun CheckWinner() {
        var winner = -1

        //row 1
        if (Player1.contains(1) && Player1.contains(2) && Player1.contains(3))
            winner = 1

        if (Player2.contains(1) && Player2.contains(2) && Player2.contains(3))
            winner = 2

        //row 2
        if (Player1.contains(4) && Player1.contains(5) && Player1.contains(6))
            winner = 1

        if (Player2.contains(4) && Player2.contains(5) && Player2.contains(6))
            winner = 2

        //row 3
        if (Player1.contains(7) && Player1.contains(8) && Player1.contains(9))
            winner = 1

        if (Player2.contains(7) && Player2.contains(8) && Player2.contains(9))
            winner = 2


        //col 1
        if (Player1.contains(1) && Player1.contains(4) && Player1.contains(7))
            winner = 1

        if (Player2.contains(1) && Player2.contains(4) && Player2.contains(7))
            winner = 2

        //col 2
        if (Player1.contains(2) && Player1.contains(5) && Player1.contains(8))
            winner = 1

        if (Player2.contains(2) && Player2.contains(5) && Player2.contains(8))
            winner = 2

        //col 3
        if (Player1.contains(3) && Player1.contains(6) && Player1.contains(9))
            winner = 1

        if (Player2.contains(3) && Player2.contains(6) && Player2.contains(9))
            winner = 2


        if (winner == 1) {
            Toast.makeText(this, "Player 1 wins", Toast.LENGTH_LONG).show()
        } else if (winner == 2) {
            Toast.makeText(this, "Player 2 wins", Toast.LENGTH_LONG).show()
        }
    }

    fun autoPlay(){
        var emptyList = ArrayList<Int>()
        for(cellID in 1..9){
            if(!Player1.contains(cellID) || Player2.contains(cellID))
                emptyList.add(cellID)
        }

        val r = Random()
        val randomIndex = r.nextInt(emptyList.size - 0) + 0

        val cellID = emptyList.get(randomIndex)

        var buSelected:Button?
        when(cellID){
            1->buSelected = bu1
            2->buSelected = bu2
            3->buSelected = bu3
            4->buSelected = bu4
            5->buSelected = bu5
            6->buSelected = bu6
            7->buSelected = bu7
            8->buSelected = bu8
            9->buSelected = bu9
            else->buSelected = bu1
        }

        PlayGame(cellID, buSelected)
    }

    fun onButtonRequest(view:View){
        var email = etEmail.text.toString()

        myRef.child("Users").child(email).child("Request").push().setValue(mEmail)
    }

    fun onButtonAccept(view:View){
        var email = etEmail.text.toString()
    }
}
