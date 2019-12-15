package com.karim.myapplication.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.karim.myapplication.Model.PhotoGraph
import com.karim.myapplication.R

class BasketRVAdapter(var _ctx:Context,var list:List<PhotoGraph>) :RecyclerView.Adapter<BasketRVAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view=LayoutInflater.from(_ctx).inflate(R.layout.baskete_item,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.rv.setHasFixedSize(true)
        holder.rv.layoutManager=LinearLayoutManager(_ctx)
        var adapter=RVAdapterPkItems(list.get(position).items,_ctx)
        holder.rv.adapter=adapter
        holder.titleText.text=list.get(position).name
    }

    class ViewHolder(itemview:View) :RecyclerView.ViewHolder(itemview){
        var titleText:TextView
        init {
            titleText=itemview.findViewById(R.id.title_text)
        }
        var rv:RecyclerView
        init {
            rv=itemview.findViewById(R.id.rv)
        }
    }
}