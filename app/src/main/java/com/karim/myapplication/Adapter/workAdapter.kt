package com.karim.myapplication.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.karim.myapplication.Model.PhotoGraph
import com.karim.myapplication.Model.WorkDone
import com.karim.myapplication.R

class workAdapter (var _ctx: Context, var list:List<WorkDone>) :RecyclerView.Adapter<workAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view= LayoutInflater.from(_ctx).inflate(R.layout.work_item,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.nameText.text=list.get(position).employeName
        holder.date.text=list.get(position).workDate
        holder.url.text=list.get(position).workURL
    }

    class ViewHolder(itemview: View) :RecyclerView.ViewHolder(itemview){
        var nameText: TextView
        init {
            nameText=itemview.findViewById(R.id.name)
        }
        var date:TextView
        init {
            date=itemview.findViewById(R.id.date)
        }
        var url:TextView
        init {
            url=itemview.findViewById(R.id.url)
        }
    }
}