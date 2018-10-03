package com.uka.mediaplayer

import android.content.Context
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.song_ticket.view.*
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    var songList = ArrayList<SongInfo>()

    var adapter: SongAdapter? = null

    var mp: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkPermission()
//        loadURLOnline()



        var trackingThread = MySongTrack()
        trackingThread.start()
    }

    fun checkPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_CODE_PERMISSION)
                return
            }
        }

        loadSongs()
    }

    private val REQUEST_CODE_PERMISSION = 123

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_CODE_PERMISSION -> if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadSongs()
            } else {
                Toast.makeText(this, "Permission denied!!", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun loadURLOnline() {
        songList.add(SongInfo("Test song title", "Test author", "https://cs1.mp3co.biz/download/5524826/aytVaXNycDYrSnp3STY5ZU9sN2dnRXJuYUlqZ2laVmx3bzVkY3lrb1RHMmVqTFo3QjQ0SzN5YW1uRjVmZHNLeFRZeGtHS1ViM2V2UEVQTkk1NHF1SjJKSkZGQU9VbVk3OGJvNmo0QjBxeWs9/Steins_Gate_OST_Gate_of_Steiner_(mp3co.biz).mp3"))
    }

    fun loadSongs() {
        val allSongs = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val selection = MediaStore.Audio.Media.IS_MUSIC + "!=0"
        val cursor = contentResolver.query(allSongs, null, selection, null, null)

        if (cursor != null) {
            if (cursor!!.moveToFirst()) {
                do {
                    val songUrl = cursor!!.getString(cursor!!.getColumnIndex(MediaStore.Audio.Media.DATA))
                    val songAuthor = cursor!!.getString(cursor!!.getColumnIndex(MediaStore.Audio.Media.ARTIST))
                    val songName = cursor!!.getString(cursor!!.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME))

                    songList.add(SongInfo(songName, songAuthor, songUrl))
                } while (cursor!!.moveToNext())
            }
        }

        adapter = SongAdapter(this, songList)
        mListSongs.adapter = adapter
    }

    inner class SongAdapter : BaseAdapter {

        var songList = ArrayList<SongInfo>()
        var context: Context? = null

        var layoutInflater: LayoutInflater? = null

        constructor(context: Context, songList: ArrayList<SongInfo>) {
            this.context = context

            this.songList = songList

            layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val myView = layoutInflater!!.inflate(R.layout.song_ticket, null)

            val song = songList[position]

            myView.mTextTitle.text = song.title

            myView.mTextAuthor.text = song.author

            myView.mButtonPlay.setOnClickListener {

                if (myView.mButtonPlay.text.equals("Play")) {

                    mp = MediaPlayer()
                    try {
                        mp!!.setDataSource(song.songURL)
                        mp!!.prepare()
                        mp!!.start()

                        myView.mButtonPlay.text = "Stop"

                        mSeekBarProgress.max = mp!!.duration
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                    }
                } else {
                    mp!!.stop()
                    myView.mButtonPlay.text = "Play"
                }
            }

            return myView
        }

        override fun getItem(position: Int): Any {
            return songList.get(position)
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return songList.size
        }

    }

    inner class MySongTrack() : Thread() {

        override fun run() {
            while (true) {
                try {
                    Thread.sleep(1000)
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
                if (mp != null) {
                    runOnUiThread {
                        mSeekBarProgress.progress = mp!!.currentPosition
                    }
                }
            }
        }
    }
}
