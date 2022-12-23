package com.j18.utility

import com.j18.trailbuddy.R
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.j18.dao.FetchBikeTrails
import com.j18.trailbuddy.list_trail_fragment
import com.squareup.picasso.Picasso
import java.util.ArrayList


class ListViewAdapter(private val onClickListener: (View, FetchBikeTrails) -> Unit,val trailList: ArrayList<FetchBikeTrails>) : RecyclerView.Adapter<ListViewAdapter.ViewHolder>() {

    var context: Context? = null
    override fun onCreateViewHolder(viewgroup : ViewGroup, position: Int): ViewHolder {
        val v = LayoutInflater.from(viewgroup?.context).inflate(R.layout.list_items_template, viewgroup, false)
        this.context = viewgroup?.context
        return ViewHolder(v);
    }
    override fun getItemCount(): Int {
        return trailList.size
    }
    override fun onBindViewHolder(viewholder: ViewHolder, position: Int) {
        val details = trailList[position];
        viewholder.name?.text = trailList[position].name
        viewholder.ratingBar?.rating = trailList[position].rating?.toFloat()!!
        if(trailList[position].thumbnail!=null) {
            Log.i("TAG","Thumbnail "+ trailList[position].thumbnail.toString()+" "+context+" "+viewholder.imageview)
            Picasso.get().load(trailList[position].thumbnail.toString()).into(viewholder.imageview);
        }
        viewholder.itemView.setOnClickListener{ view->
            onClickListener.invoke(view,details )

        }
    }
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name = itemView.findViewById<TextView>(R.id.trailName)
        val ratingBar = itemView.findViewById<RatingBar>(R.id.ratingBar)
        val imageview = itemView.findViewById<ImageView>(R.id.itemIcon)

    }
}