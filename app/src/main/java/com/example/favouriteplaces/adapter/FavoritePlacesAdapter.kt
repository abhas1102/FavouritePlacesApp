package com.example.favouriteplaces.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.favouriteplaces.R
import com.example.favouriteplaces.models.FavoritePlaceModel
import kotlinx.android.synthetic.main.item_favorite_place.view.*

open class FavoritePlacesAdapter(private val context: Context,
                                 private var list: ArrayList<FavoritePlaceModel>):RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(
            R.layout.item_favorite_place,parent,false
        ))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]
        if (holder is MyViewHolder){
            holder.itemView.iv_place_image.setImageURI(Uri.parse(model.image))
            holder.itemView.tvTitle.text = model.title
            holder.itemView.tvDescription.text = model.description
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    private class MyViewHolder(view:View):RecyclerView.ViewHolder(view){

    }

}