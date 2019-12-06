package com.karim.myapplication.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.karim.myapplication.Model.TypesItems
import com.karim.myapplication.R

class RvAdapter(var typesItemsList: ArrayList<TypesItems>, var _ctx: Context) :
    RecyclerView.Adapter<RvAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(_ctx).inflate(R.layout.rv_item1, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.item.text = typesItemsList[position].item
        holder.deleteIcon.setOnClickListener{
            typesItemsList.removeAt(position)
            notifyItemChanged(position)
            notifyItemRangeChanged(position,typesItemsList.size)
        }
    }

    override fun getItemCount(): Int {
        return typesItemsList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
         var item: TextView
        init {
            item = itemView.findViewById(R.id.item_text)
        }
        var deleteIcon:ImageView
        init {
            deleteIcon=itemView.findViewById(R.id.delete_icon)
        }
    }
}
