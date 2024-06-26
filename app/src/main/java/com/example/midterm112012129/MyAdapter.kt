package com.example.midterm112012129

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

class MyAdapter(context: Context, data: ArrayList<Character>, private val layout: Int) : ArrayAdapter<Character>(context, layout, data) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val view = View.inflate(parent.context, layout, null)

        val character = getItem(position) ?: return view

        val img_photo = view.findViewById<ImageView>(R.id.img_photo)
        img_photo.setImageResource(character.image)

        val tv_msg = view.findViewById<TextView>(R.id.tv_msg)
        tv_msg.text = if (layout == R.layout.adapter_vertical)
            character.name
        else
            character.name

        return view
    }
}
