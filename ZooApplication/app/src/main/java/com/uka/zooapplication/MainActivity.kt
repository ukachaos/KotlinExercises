package com.uka.zooapplication

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.row.view.*

class MainActivity : AppCompatActivity() {

    var listOfAnimals = ArrayList<Row>()

    var adapter: AnimalAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //load Animals
        listOfAnimals.add(Row("Baboon", "Baboon that lives in zoo", R.drawable.baboon, false))
        listOfAnimals.add(Row("Bulldog", "Bulldog that lives in zoo", R.drawable.bulldog, true))
        listOfAnimals.add(Row("Panda", "Panda that lives in zoo", R.drawable.panda, false))
        listOfAnimals.add(Row("Swallow bird", "Swallow bird that lives in zoo", R.drawable.swallow_bird, false))
        listOfAnimals.add(Row("White tiger", "White tiger that lives in zoo", R.drawable.white_tiger, true))
        listOfAnimals.add(Row("Zebra", "Zebra that lives in zoo", R.drawable.zebra, false))

        adapter = AnimalAdapter(this, listOfAnimals)

        listView.adapter = adapter
    }

    fun deleteFromListView(index:Int){
        listOfAnimals.removeAt(index)
        adapter!!.notifyDataSetChanged()
    }

    fun addToListView(index:Int){
        listOfAnimals.add(index, listOfAnimals.get(index))
        adapter!!.notifyDataSetChanged()
    }

    inner class AnimalAdapter : BaseAdapter {

        var listOfAnimals = ArrayList<Row>()

        var context: Context? = null

        var layoutInflator: LayoutInflater? = null

        constructor(context: Context, listOfAnimals: ArrayList<Row>) : super() {
            this.listOfAnimals = listOfAnimals
            this.context = context

            layoutInflator = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater?
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val animal = listOfAnimals[position]
            var view:View?=null
            if (animal.isKiller == false) {
                view = layoutInflator!!.inflate(R.layout.row, null)
                view.tvName.text = animal.name
                view.tvDes.text = animal.des
                view.imageView.setImageResource(animal.image!!)
            } else {
                view = layoutInflator!!.inflate(R.layout.row_red, null)
                view.tvName.text = animal.name
                view.tvDes.text = animal.des
                view.imageView.setImageResource(animal.image!!)
            }

            view.setOnClickListener{
//                var intent:Intent=Intent(context, Details::class.java)
//
//                intent.putExtra("name", animal.name)
//                intent.putExtra("des", animal.des)
//                intent.putExtra("image", animal.image!!)
//                context!!.startActivity(intent)

//                deleteFromListView(position)

                addToListView(position)
            }

            return view
        }

        override fun getItem(position: Int): Any {
            return listOfAnimals[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return listOfAnimals.size
        }

    }
}
