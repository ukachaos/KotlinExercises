package com.uka.tictactoe

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception
import java.util.*
import kotlin.collections.HashMap

class MainActivity : AppCompatActivity() {

    private var mFirebaseAnalytics: FirebaseAnalytics? = null

    private var mDatabase = FirebaseDatabase.getInstance()

    private var myRef = mDatabase.reference

    var mEmail: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)

        mEmail = intent.extras.getString("email")

        incomingCalls()
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

//        PlayGame(cellId, buSelected)

        myRef.child("PlayerOnline").child(sessionID).child(cellId.toString()).setValue(mEmail)
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

    fun autoPlay(cellID: Int) {

        var buSelected: Button?
        when (cellID) {
            1 -> buSelected = bu1
            2 -> buSelected = bu2
            3 -> buSelected = bu3
            4 -> buSelected = bu4
            5 -> buSelected = bu5
            6 -> buSelected = bu6
            7 -> buSelected = bu7
            8 -> buSelected = bu8
            9 -> buSelected = bu9
            else -> buSelected = bu1
        }

        PlayGame(cellID, buSelected)
    }

    fun onButtonRequest(view: View) {
        var email = etEmail.text.toString()

        myRef.child("Users").child(splitString(email)).child("Request").push().setValue(mEmail)

        playerOnline(splitString(mEmail!!) + splitString(email))

        playerSymbol = "X"
    }

    fun onButtonAccept(view: View) {
        var email = etEmail.text.toString()
        myRef.child("Users").child(splitString(email)).child("Request").push().setValue(mEmail)

        playerOnline(splitString(email) + splitString(mEmail!!))

        playerSymbol = "O"
    }

    var number = 0
    fun incomingCalls() {
        myRef.child("Users").child(splitString(mEmail!!)).child("Request")
                .addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError?) {
                    }

                    override fun onDataChange(dataSnapshot: DataSnapshot?) {
                        try {
                            var td = dataSnapshot!!.value as HashMap<String, Any>
                            if (td != null) {
                                var value: String
                                for (key in td.keys) {
                                    value = td[key] as String
                                    etEmail.setText(value)

                                    val notifyMe = Notifications()
                                    notifyMe.Notify(applicationContext, value + "wants to play!", number)
                                    number++

                                    myRef.child("Users").child(splitString(mEmail!!)).child("Request").setValue(true)
                                    break
                                }
                            }
                        } catch (ex: Exception) {
                            ex.printStackTrace()
                        }
                    }

                })
    }

    fun splitString(str: String): String {
        var split = str.split("@")
        return split[0]
    }

    var sessionID: String? = null
    var playerSymbol: String? = null

    fun playerOnline(sessionID: String) {
        this.sessionID = sessionID

        myRef.child("PlayerOnline").removeValue()

        myRef.child("PlayerOnline").child(sessionID)
                .addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError?) {
                    }

                    override fun onDataChange(dataSnapshot: DataSnapshot?) {
                        Player1.clear()
                        Player2.clear()
                        try {
                            var td = dataSnapshot!!.value as HashMap<String, Any>
                            if (td != null) {
                                var value: String
                                for (key in td.keys) {
                                    value = td[key] as String
                                    if (value != mEmail) {
                                        activePlayer = if (playerSymbol.equals("X")) 1 else 2
                                    } else {
                                        activePlayer = if (playerSymbol.equals("X")) 2 else 1
                                    }

                                    autoPlay(key.toInt())
                                }
                            }
                        } catch (ex: Exception) {
                            ex.printStackTrace()
                        }
                    }

                })
    }
}
