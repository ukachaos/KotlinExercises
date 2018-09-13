package com.uka.noteapp

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.BaseAdapter
import android.widget.SearchView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.ticket.view.*

class MainActivity : AppCompatActivity() {

    var listOfNotes = ArrayList<Note>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        listOfNotes.add(Note(1, "Note Luke", "I am your father!!!"))
//        listOfNotes.add(Note(2, "Harry Potter", "Deathly Hollows"))
//        listOfNotes.add(Note(3, "Matrix", "Reloaded"))

        var noteAdapter = NoteAdapter(this, listOfNotes)
        lvNotes.adapter = noteAdapter

        loadData("%")
    }

    fun loadData(title: String) {
        var dbManager = DBManager(this)
        val projection = arrayOf(dbManager.colID, dbManager.colTitle, dbManager.colDes)
        val selectionArgs = arrayOf(title)
        val cursor = dbManager.Query(projection, "title like ?", selectionArgs, "title")

        listOfNotes.clear()

        if (cursor.moveToFirst()) {
            do {
                val ID = cursor.getInt(cursor.getColumnIndex(dbManager.colID))
                val title = cursor.getString(cursor.getColumnIndex(dbManager.colTitle))
                val description = cursor.getString(cursor.getColumnIndex(dbManager.colDes))

                listOfNotes.add(Note(ID, title, description))

            } while (cursor.moveToNext())
        }

        var noteAdapter = NoteAdapter(this, listOfNotes)
        lvNotes.adapter = noteAdapter
    }

    inner class NoteAdapter : BaseAdapter {

        var context: Context? = null
        var layoutInflater: LayoutInflater? = null

        var listNote = ArrayList<Note>()

        constructor(context: Context, listNote: ArrayList<Note>) : super() {
            this.context = context
            this.listNote = listNote

            layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            var note = listNote[position]

            var view = layoutInflater!!.inflate(R.layout.ticket, null)
            view.tvTitle.text = note.noteName
            view.tvDes.text = note.noteDes

            return view
        }

        override fun getItem(position: Int): Any {
            return listNote[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return listNote.size
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)

        val sv = menu!!.findItem(R.id.menu_search).actionView as SearchView
        val sm = getSystemService(Context.SEARCH_SERVICE) as SearchManager

        sv.setSearchableInfo(sm.getSearchableInfo(componentName))
        sv.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                loadData("%$newText%")
                return false
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
        })

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.menu_add_note -> {
                var intent = Intent(this, AddNotes::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
