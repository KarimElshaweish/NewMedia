package com.karim.myapplication.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.karim.myapplication.Model.TypesItems
import com.karim.myapplication.R

class RVItemsAdapter(var _ctx:Context,var itemList:ArrayList<TypesItems>)
    :RecyclerView.Adapter<RVItemsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RVItemsAdapter.ViewHolder {
        var v=LayoutInflater.from(_ctx).inflate(R.layout.item_pk_list,parent,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: RVItemsAdapter.ViewHolder, position: Int) {
        holder.itemText.setText(itemList.get(position).item)
    }

    inner class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        var itemText:TextView
       init {
           itemText=itemView.findViewById(R.id.item_text_list)
       }
    }
}