package com.uka.findmyphone

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import android.widget.Toast

class LoginActivity : AppCompatActivity() {

    var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mAuth = FirebaseAuth.getInstance()

        singInAnonymously()
    }

    fun singInAnonymously() {

        mAuth!!.signInAnonymously().addOnCompleteListener {
            if (it.isSuccessful()) {
                // Sign in success, update UI with the signed-in user's information
                Toast.makeText(applicationContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
                val user = mAuth!!.getCurrentUser()
            } else {
                Log.e(">>>>>>>", it.exception!!.message.toString())
                // If sign in fails, display a message to the user.
                Toast.makeText(applicationContext, "Authentication failed." + it.toString(), Toast.LENGTH_SHORT).show()
            }

        }
    }

    fun onClickRegister(view: View) {
        val userData = UserData(this)
        userData.savePhone(mEditPhone.text.toString())

        finish()
    }
}
