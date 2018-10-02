package com.uka.twitterdemo

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Toast
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.add_ticket.view.*
import kotlinx.android.synthetic.main.tweets_ticket.view.*
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class MainActivity : AppCompatActivity() {

    val REQUEST_PICKIMAGE: Int = 345

    var email: String? = null
    var uid: String? = null

    private var mFirebaseAnalytics: FirebaseAnalytics? = null

    private var database = FirebaseDatabase.getInstance()
    private var myRef = database.reference

    var tweets = ArrayList<Tweet>()

    var adapter: TwittersAdapter? = null

    var imageDownloadURL: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var b: Bundle = intent.extras

        email = b.getString("email")
        uid = b.getString("uid")

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)

        tweets.add(Tweet("", "Test", "test", "add"))

        adapter = TwittersAdapter(this, tweets)

        mListTweets.adapter = adapter

        loadPost()
    }

    fun loadImage() {
        var intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
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
            uploadImage(BitmapFactory.decodeFile(picturePath))
        }
    }

    fun uploadImage(bitmap: Bitmap) {

        val storage = FirebaseStorage.getInstance()

        val storageRef = storage.getReferenceFromUrl("gs://twitterdemo-6c430.appspot.com")
        val df = SimpleDateFormat("ddMMyyHHmmss")
        val dateObject = Date()
        val imagePath = email!!.toString().split("@")[0] + "_" + df.format(dateObject) + ".jpg"

        val imageRef = storageRef.child("imagePost/" + imagePath)
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)

        val data = baos.toByteArray()

        val uploadTask = imageRef.putBytes(data)
        uploadTask.addOnFailureListener {
            Toast.makeText(applicationContext, "Failed to upload image", Toast.LENGTH_LONG).show()
        }.addOnSuccessListener { taskSnapshot ->
            Toast.makeText(applicationContext, "Upload success", Toast.LENGTH_LONG).show()
            taskSnapshot.storage.downloadUrl.addOnSuccessListener {
                imageDownloadURL = it.toString()
            }
        }
    }

    private fun loadPost() {
        myRef.child("posts").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                try {
                    tweets.clear()
                    tweets.add(Tweet("", "Test", "test", "add"))

                    var td = dataSnapshot!!.value as HashMap<String, Any>
                    for (key in td.keys) {
                        var post = td[key] as HashMap<String, Any>

                        tweets.add(Tweet(key, post["text"] as String, post["imageDownloadURL"] as String, post["uid"] as String))
                    }

                    adapter!!.notifyDataSetChanged()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onCancelled(p0: DatabaseError) {
            }
        })
    }

    inner class TwittersAdapter : BaseAdapter {

        var tweets = ArrayList<Tweet>()
        var context: Context? = null

        var inflater: LayoutInflater? = null

        constructor(context: Context, tweets: ArrayList<Tweet>) {
            this.context = context
            this.tweets = tweets

            inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

            if (convertView == null) {

                var tweet = tweets[position]

                if (tweet.tweetPersonUID.equals("add")) {
                    var mView = inflater!!.inflate(R.layout.add_ticket, null)

                    mView.mImageAttach.setOnClickListener {
                        loadImage()
                    }

                    mView.mImageSend.setOnClickListener {
                        myRef.child("posts").push().setValue(PostInfo(uid!!, mView.mEditPost.text.toString(), imageDownloadURL!!))

                        mView.mEditPost.setText("")
                    }

                    return mView
                } else {
                    var mView = inflater!!.inflate(R.layout.tweets_ticket, null)
                    mView.txtUserName.text = tweet.tweetPersonUID
                    mView.txt_tweet.text = tweet.tweetText

                    Picasso.get().load(tweet.tweetImageUrl).into(mView.tweet_picture)

                    myRef.child("Users").child(tweet.tweetPersonUID!!).addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            try {
                                var td = dataSnapshot!!.value as HashMap<String, Any>
                                for (key in td.keys) {
                                    var userInfo = td[key] as String

                                    if(key.equals("Email")){
                                        mView.txtUserName.text = userInfo.split("@")[0]
                                    }else{
                                        Picasso.get().load(userInfo).into(mView.picture_path)
                                    }
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }

                        override fun onCancelled(p0: DatabaseError) {
                        }
                    })

                    return mView
                }
            } else return convertView!!
        }

        override fun getItem(position: Int): Any {
            return tweets[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return tweets.size
        }


    }
}
