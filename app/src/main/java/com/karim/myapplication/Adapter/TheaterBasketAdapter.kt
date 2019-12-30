package com.karim.myapplication.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.karim.myapplication.R
import com.karim.myapplication.Util
import com.karim.myapplication.model.ScreenType
import com.karim.myapplication.model.TheaterData

class TheaterBasketAdapter(var _ctx:Context,var theaterList:MutableList<TheaterData>)
    :RecyclerView.Adapter<TheaterBasketAdapter.ViewHolder>() {

    class ViewHolder(itemview:View):RecyclerView.ViewHolder(itemview) {
        var titleText: TextView = itemview.findViewById(R.id.title_text)
        var rv:RecyclerView = itemview.findViewById(R.id.rv)
        var delete: ImageView = itemview.findViewById(R.id.delete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       var view=LayoutInflater.from(_ctx).inflate(R.layout.baskete_item,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount()=theaterList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.titleText.text = theaterList.get(position).totalMeter + " متر "
        holder.delete.setOnClickListener {
            if (theaterList != null) {
                theaterList.toMutableList().removeAt(position)
                Util.list.removeAt(position)
                notifyItemChanged(position)
                notifyItemRangeChanged(position, theaterList.size)
            }
        }
    }
}