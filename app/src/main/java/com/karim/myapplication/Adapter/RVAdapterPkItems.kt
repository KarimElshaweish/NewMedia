package com.karim.myapplication.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.karim.myapplication.Model.TypesItems
import com.karim.myapplication.R
import java.lang.reflect.Type
import java.util.*
import kotlin.collections.HashMap

class RVAdapterPkItems(var typesItemsList: List<TypesItems>, var _ctx: Context): RecyclerView.Adapter<RVAdapterPkItems.ViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var v=LayoutInflater.from(_ctx).inflate(R.layout.adding_item_pk,parent,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {

        return typesItemsList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        var t=typesItemsList.get(position) as HashMap<String,String>
        holder.text.text = t.get("item") as String
    }

    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        var text:TextView
        init {
            text=itemView.findViewById(R.id.item_text)
        }


    }

}