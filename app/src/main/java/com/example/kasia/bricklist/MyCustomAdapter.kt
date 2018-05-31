package com.example.kasia.bricklist

import android.app.Activity
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color.*
import android.provider.ContactsContract
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
/**
 * Created by Kasia on 26.05.2018.
 */
class MyCustomAdapter(private var activity: Activity, private var items: ArrayList<InventoriesPart>, private var context: Context) : BaseAdapter(){
    var database = DataBaseHelper(context)
    private class ViewHolder(row: View?){

        var txtName: TextView? = null
        var available: TextView?= null
        var separator: TextView? = null
        var needed: TextView?=null
        var image: ImageView?=null
        var plusButton: Button? =null
        var minusButton: Button? = null
        var layout: RelativeLayout? = null

        init{
            this.txtName = row?.findViewById<TextView>(R.id.txtName)
            this.available = row?.findViewById<TextView>(R.id.available)
            this.needed= row?.findViewById<TextView>(R.id.needed)
            this.image = row?.findViewById<ImageView>(R.id.imageView)
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

        var inventoryPart = items[position]
        if(inventoryPart.name!!.length>0)
            viewHolder.txtName?.text = inventoryPart.name
        else if(viewHolder.txtName?.text!!.length>0)
            viewHolder.txtName?.text = inventoryPart.itemId
        else
            viewHolder.txtName?.text = inventoryPart.itemType


        viewHolder.available?.text = inventoryPart.quantityInStore.toString()
        viewHolder.needed?.text = inventoryPart.quantityInSet.toString()

        if(inventoryPart.image!=null){
            val bitmap = BitmapFactory.decodeByteArray(inventoryPart.image, 0, inventoryPart.image!!.size)
            viewHolder.image?.setImageBitmap(bitmap)
        }

        viewHolder.plusButton?.setOnClickListener{
            if( inventoryPart.quantityInStore < inventoryPart.quantityInSet){
                inventoryPart.quantityInStore+=1
                viewHolder.available?.text = inventoryPart.quantityInStore.toString()
                //update bazy danych fun updateInventoriesPart(inventoryID:Int, itemID:Int, quantityInStore: Int, colorID: Int)
                database.updateInventoriesPart(items[position].inventoryID!!, items[position].itemId!!,items[position].quantityInStore!!, items[position].colorID!!)
                if(inventoryPart.quantityInStore==inventoryPart.quantityInSet){
                    viewHolder.layout?.setBackgroundColor(GREEN)
                }
            }
            else{
                Toast.makeText(context, "Masz juz tyle klockow, ile potrzebujesz", Toast.LENGTH_LONG).show()
            }

        }

        viewHolder.minusButton?.setOnClickListener{
            if(inventoryPart.quantityInStore>0) {
                inventoryPart.quantityInStore -= 1
                viewHolder.available?.text = inventoryPart.quantityInStore.toString()
                //update bazy
                database.updateInventoriesPart(items[position].inventoryID!!, items[position].itemId!!, items[position].quantityInStore!!, items[position].colorID!!)
                if (inventoryPart.quantityInStore != inventoryPart.quantityInSet) {
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
