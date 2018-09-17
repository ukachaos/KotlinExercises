package com.uka.tictactoe

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private var mAuth: FirebaseAuth? = null

    private var mDatabase = FirebaseDatabase.getInstance()

    private var myRef = mDatabase.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mAuth = FirebaseAuth.getInstance()



        loadMain()
    }

    fun onButtonLogin(view: View) {
        var email = etEmail.text.toString()
        var password = etPassword.text.toString()

        loginToFirebase(email, password)
    }

    fun loginToFirebase(email: String, password: String) {
        mAuth!!.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(applicationContext, "Login successful", Toast.LENGTH_LONG).show()
                        var currentUser = mAuth!!.currentUser
                        if (currentUser != null) {
                            //save in firebase database
                            myRef.child("Users").child(splitString(currentUser.email.toString())).child("Request").setValue(currentUser.uid)
                        }

                        loadMain()
                    } else {
                        Toast.makeText(applicationContext, "Login fail", Toast.LENGTH_LONG).show()
                    }
                }
    }

    fun loadMain() {
        var currentUser = mAuth!!.currentUser

        if (currentUser != null) {

            var intent = Intent(this, MainActivity::class.java)
            intent.putExtra("email", currentUser.email)
            intent.putExtra("uid", currentUser.uid)
            startActivity(intent)

            finish()
        }
    }

    fun splitString(str:String):String{
        var split = str.split("@")
        return split[0]
    }
}
