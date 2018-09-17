package com.uka.firebasedemoapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private var mFirebaseAnalytics: FirebaseAnalytics? = null

    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)

        mAuth = FirebaseAuth.getInstance()
    }

    fun buLogin(view: View) {
        val email = etEmail.text.toString()
        val password = etPassword.text.toString()

        loginToFirebase(email, password)
    }

    fun loginToFirebase(email: String, password: String) {
        mAuth!!.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this){task->
            if(task.isSuccessful){
                Toast.makeText(applicationContext, "Login successful", Toast.LENGTH_LONG).show()
                val currentUser = mAuth!!.currentUser
                Log.e("login", currentUser!!.uid)
            }
            else{
                Toast.makeText(applicationContext, "Login fail", Toast.LENGTH_LONG).show()
            }

        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = mAuth!!.currentUser

        if(currentUser!= null){
            val intent = Intent(this, Control::class.java)
            startActivity(intent)
        }
    }
}
