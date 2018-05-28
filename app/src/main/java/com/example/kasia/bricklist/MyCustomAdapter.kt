package com.example.kasia.bricklist

import android.app.Activity
import android.content.Context
import android.graphics.Color.*
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
/**
 * Created by Kasia on 26.05.2018.
 */
class MyCustomAdapter(private var activity: Activity, private var items: ArrayList<String>, private var context: Context) : BaseAdapter(){

    private class ViewHolder(row: View?){

        var txtName: TextView? = null
        var available: TextView?= null
        var separator: TextView? = null
        var needed: TextView?=null
        //var image: ImageView?=null
        var plusButton: Button? =null
        var minusButton: Button? = null
        var layout: RelativeLayout? = null

        init{
            this.txtName = row?.findViewById<TextView>(R.id.txtName)
            this.available = row?.findViewById<TextView>(R.id.available)
            //this.separator = row?.findViewById<TextView>(R.id.separator)
            this.needed= row?.findViewById<TextView>(R.id.needed)
            //this.image = row?.findViewById<ImageView>(R.id.imageView)
            this.layout = row?.findViewById<RelativeLayout>(R.id.layout)
            this.plusButton = row?.findViewById<Button>(R.id.plusButton)
            this.minusButton = row?.findViewById<Button>(R.id.minusButton)
        }
    }


    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View?
        val viewHolder: ViewHolder

        if(convertView == null){
            val inflater = activity?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.set_element, null)
            viewHolder = ViewHolder(view)
            view?.tag=viewHolder
        } else{
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        var userDto = items[position]
        viewHolder.txtName?.text = userDto
        //viewHolder.available?.text

        viewHolder.plusButton?.setOnClickListener{
            var av = viewHolder.available?.text.toString().toInt()
            val need = viewHolder.needed?.text.toString().toInt()
            if( av < need){
                av=av+1
                viewHolder.available?.text = av.toString()
                if(av==need){
                    viewHolder.layout?.setBackgroundColor(GREEN)
                }
            }
            else{
                Toast.makeText(context, "Masz juz tyle klockow, ile potrzebujesz", Toast.LENGTH_LONG).show()
            }

        }

        viewHolder.minusButton?.setOnClickListener{
            var av = viewHolder.available?.text.toString().toInt()
            val need = viewHolder.needed?.text.toString().toInt()
            if(av>0) {
                av = av - 1
                viewHolder.available?.text = av.toString()
                if (av != need) {
                    viewHolder.layout?.setBackgroundColor(TRANSPARENT)
                }
            }
            else {
                Toast.makeText(context, "Nie mozesz miec mniej niz 0 klockow", Toast.LENGTH_LONG).show()
            }

        }


        return view as View
    }

    override fun getItem(p0: Int): Any {
        return items[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getCount(): Int {
        return items.size
    }


}
