package com.karim.myapplication.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.karim.myapplication.model.PhotoGraph
import com.karim.myapplication.R
import com.karim.myapplication.Util
import com.mikhaellopez.circularimageview.CircularImageView

class CardAdapter (var photList: List<PhotoGraph>,var _ctx:Context,var nochoice:Boolean)
    :RecyclerView.Adapter<CardAdapter.ViewHolder> (){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(_ctx).inflate(R.layout.item, parent, false)


        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
         return  photList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text=photList.get(position).name
        holder.price.text=photList.get(position).price
        holder.rv.setHasFixedSize(true)
        holder.rv.layoutManager=LinearLayoutManager(_ctx)
        var adapter=RVAdapterPkItems(photList.get(position).items,_ctx)
        holder.rv.adapter=adapter
        if(nochoice)
            holder.button.visibility=View.GONE
        else{
            holder.button.setOnClickListener{
                Toast.makeText(_ctx,"تمت الاضافة",Toast.LENGTH_LONG).show()
                Util.list.add(photList.get(position))
            }
        }
        holder.pkIamge.elevation=10f
        holder.cv.elevation=9f
        Glide.with(_ctx).load(photList.get(position).image).placeholder(_ctx.resources.getDrawable(R.drawable.defualt)
        ).into(holder.pkIamge)
    }

    class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        val title = itemView.findViewById<TextView>(R.id.title)
        val price = itemView.findViewById<TextView>(R.id.price)
        val  rv=itemView.findViewById<RecyclerView>(R.id.rv)
        var button=itemView.findViewById<Button>(R.id.button)
        var pkIamge=itemView.findViewById<CircularImageView>(R.id.pkIamge)
        var cv=itemView.findViewById<CardView>(R.id.cv)
    }
}