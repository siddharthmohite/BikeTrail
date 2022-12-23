package com.j18.utility

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.j18.dao.FetchBikeTrails
import com.j18.db.Trail
import com.j18.trailbuddy.R
import java.util.ArrayList


class RecentlyViewedAdapter(private val context: Context, private val arrayList: ArrayList<Trail>) : BaseAdapter() {
    private lateinit var name: TextView

    override fun getCount(): Int {
        return arrayList.size
    }
    override fun getItem(position: Int): Any {
        return position
    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        var convertView = convertView
        convertView = LayoutInflater.from(context).inflate(R.layout.recently_viewed_item, parent, false)
        name = convertView.findViewById(R.id.name)
        //    length = convertView.findViewById(R.id.length)
        name.text = arrayList[position].trailName
        //    length.text = arrayList[position].length
        return convertView
    }
}