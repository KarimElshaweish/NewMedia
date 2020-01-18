package com.karim.myapplication.Adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.florent37.expansionpanel.ExpansionHeader
import com.github.florent37.expansionpanel.ExpansionLayout
import com.karim.myapplication.R
import com.karim.myapplication.model.WorkDone


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
        holder.date.setOnClickListener{
            val uri=Uri.parse(holder.date.text.toString())
            val intent=Intent(Intent.ACTION_VIEW,uri)
            val bundle=Bundle()
            bundle.putBoolean("new_window",true)
            intent.putExtras(bundle)
            _ctx.startActivity(intent)
        }
        holder.workName.text=list.get(position).workName
        if(position%2==0) {
            holder.exp.setBackgroundColor(Color.parseColor("#dfe6e9"))
            holder.expansionLayout.setBackgroundColor(Color.parseColor("#dfe6e9"))
        }
    }

    class ViewHolder(itemview: View) :RecyclerView.ViewHolder(itemview){
        val exp: ExpansionHeader =itemview.findViewById(R.id.exp)
        val expansionLayout:ExpansionLayout=itemview.findViewById(R.id.expansionLayout)
        var nameText: TextView = itemview.findViewById(R.id.name)
        var date:TextView = itemview.findViewById(R.id.date)
        var url:TextView = itemview.findViewById(R.id.url)
        var workName:TextView = itemview.findViewById(R.id.workName)
        var image=itemview.findViewById<ImageView>(R.id.image)
    }
}