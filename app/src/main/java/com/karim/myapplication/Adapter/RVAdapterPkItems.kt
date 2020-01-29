package com.karim.myapplication.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.karim.myapplication.model.TypesItems
import com.karim.myapplication.R
import kotlin.collections.HashMap

class RVAdapterPkItems(var typesItemsList: List<TypesItems>, var _ctx: Context,var hash:Boolean): RecyclerView.Adapter<RVAdapterPkItems.ViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var v=LayoutInflater.from(_ctx).inflate(R.layout.adding_item_pk,parent,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {

        return typesItemsList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if(hash) {
            val t = typesItemsList[position] as HashMap<*, *>
            holder.text.text = t["item"] as String
        }else{
            val t = typesItemsList[position]
            holder.text.text = t.item
        }
    }

    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        var text:TextView
        init {
            text=itemView.findViewById(R.id.item_text)
        }


    }

}