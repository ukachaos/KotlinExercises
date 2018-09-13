package com.uka.foodapp

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.food_ticket.view.*

class MainActivity : AppCompatActivity() {

    var listOfFoods = ArrayList<Food>()

    var foodAdapter:FoodAdapter?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //load foods
        listOfFoods.add(Food("Coffee", "맛있는 커피", R.drawable.coffee_pot))
        listOfFoods.add(Food("Espresso", "에스프레소", R.drawable.espresso))
        listOfFoods.add(Food("French fries", "프렌츠 프라이", R.drawable.french_fries))
        listOfFoods.add(Food("Honey", "하니 하니 베이비", R.drawable.honey))
        listOfFoods.add(Food("Strawberry", "딸기 맛 아이스크림은 맛있어", R.drawable.strawberry_ice_cream))
        listOfFoods.add(Food("Sugar cubes", "설탕은 맛있지만, 나빠", R.drawable.sugar_cubes))

        foodAdapter = FoodAdapter(this, listOfFoods)

        gvFoods.adapter = foodAdapter
    }

    inner class FoodAdapter:BaseAdapter{

        var listOfFood = ArrayList<Food>()

        var context:Context?=null

        var layoutInflater:LayoutInflater?=null

        constructor(context:Context, listOfFood:ArrayList<Food>):super(){
            this.context = context
            this.listOfFood = listOfFood

            layoutInflater = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            var food = listOfFood[position]
            var foodView = layoutInflater!!.inflate(R.layout.food_ticket, null)
            foodView.imageView.setImageResource(food.image!!)
            foodView.textView.setText(food.name)
            return foodView
        }

        override fun getItem(position: Int): Any {
            return listOfFood[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return listOfFood!!.size
        }

    }
}
