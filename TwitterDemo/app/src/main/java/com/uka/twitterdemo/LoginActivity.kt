package com.uka.twitterdemo

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*


class LoginActivity : AppCompatActivity() {

    val REQUEST_READIMAGE: Int = 234
    val REQUEST_PICKIMAGE: Int = 345

    private var mAuth: FirebaseAuth? = null

    private var database = FirebaseDatabase.getInstance()
    private var myRef = database.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mAuth = FirebaseAuth.getInstance()

        mImageIcon.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                checkPermission()
            }
        })

        FirebaseMessaging.getInstance().subscribeToTopic("news")
    }

    fun loginToFirebase(email: String, password: String) {
        mAuth!!.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(applicationContext, "Login successful", Toast.LENGTH_LONG).show()

                        var currentUser = mAuth!!.currentUser
                        uploadImageToServer(currentUser!!)
                    } else {
                        Toast.makeText(applicationContext, "Login fail", Toast.LENGTH_LONG).show()
                    }
                }
    }

    fun checkPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_READIMAGE)
                return
            }
        }

        loadImage()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        when (requestCode) {
            REQUEST_READIMAGE -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    loadImage()
                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_LONG).show()
                }
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    fun loadImage() {
        var intent = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_PICKIMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_PICKIMAGE && resultCode == Activity.RESULT_OK && data != null) {
            val selectedImage = data.data
            val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
            val cursor = contentResolver.query(selectedImage, filePathColumn, null, null, null)
            cursor.moveToFirst()

            val columnIndex = cursor.getColumnIndex(filePathColumn[0])
            val picturePath = cursor.getString(columnIndex)
            cursor.close()
            mImageIcon.setImageBitmap(BitmapFactory.decodeFile(picturePath))
        }
    }

    fun onLogin(view: View) {
        if (mEditEmail.text.isNotEmpty() && mEditPassword.text.isNotEmpty()) {
            loginToFirebase(mEditEmail.text.toString(), mEditPassword.text.toString())
        }
    }

    fun uploadImageToServer(mCurrentUser: FirebaseUser) {
        val storage = FirebaseStorage.getInstance()

        val storageRef = storage.getReferenceFromUrl("gs://twitterdemo-6c430.appspot.com")
        val df = SimpleDateFormat("ddMMyyHHmmss")
        val dateObject = Date()
        val imagePath = mCurrentUser.email!!.toString().split("@")[0] + "_" + df.format(dateObject) + ".jpg"

        val imageRef = storageRef.child("images/" + imagePath)

        val drawable = mImageIcon.drawable as BitmapDrawable

        val bitmap = drawable.bitmap

        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)

        val data = baos.toByteArray()

        val uploadTask = imageRef.putBytes(data)
        uploadTask.addOnFailureListener {
            Toast.makeText(applicationContext, "Failed to upload image", Toast.LENGTH_LONG).show()
        }.addOnSuccessListener { taskSnapshot ->
            Toast.makeText(applicationContext, "Upload success", Toast.LENGTH_LONG).show()
            taskSnapshot.storage.downloadUrl.addOnSuccessListener {
                try {
                    myRef.child("Users").child(mCurrentUser.uid).child("Email").setValue(mCurrentUser.email)
                    myRef.child("Users").child(mCurrentUser.uid).child("ProfileImage").setValue(taskSnapshot.metadata!!.reference!!.downloadUrl.toString())

                    loadTweets()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        loadTweets()
    }

    fun loadTweets() {
        var mCurrentUser = mAuth!!.currentUser

        if (mCurrentUser != null) {
            var intent = Intent(this, MainActivity::class.java)
            intent.putExtra("email", mCurrentUser.email)
            intent.putExtra("uid", mCurrentUser.uid)

            startActivity(intent)

            finish()
        }
    }
}
