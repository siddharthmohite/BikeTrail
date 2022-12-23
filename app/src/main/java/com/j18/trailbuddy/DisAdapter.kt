package com.j18.trailbuddy

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso


class DisAdapter(private val disList: ArrayList<DiscussionModel>) : RecyclerView.Adapter<DisAdapter.ViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DisAdapter.ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.items,parent,false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: DisAdapter.ViewHolder, position: Int) {
        val currentComment=disList[position]
        holder.comments.text=currentComment.comments
        val imageUrl=disList[position]
        val currentImageUrl=imageUrl.imageUrl
        Picasso.get().load(currentImageUrl).into(holder.image)

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val comments  : TextView =itemView.findViewById(R.id.comments)
        val imageUrl  : TextView =itemView.findViewById(R.id.imageUrl)
        val image     : ImageView =itemView.findViewById(R.id.image)

    }

    override fun getItemCount(): Int {

        return disList.size

    }



}