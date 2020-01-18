package com.karim.myapplication.Adapter

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.github.florent37.expansionpanel.ExpansionHeader
import com.google.android.material.snackbar.Snackbar
import com.karim.myapplication.R
import com.karim.myapplication.ViewModel.OrdersViewModel
import com.karim.myapplication.model.OrderData
import com.karim.myapplication.model.TheaterUploadData
import kotlinx.android.synthetic.main.fragment_order.view.*

class OrderAdapter(var _ctx:Fragment,var items:MutableList<OrderData>,var removeBoolean: Boolean) :RecyclerView.Adapter<OrderAdapter.ViewHolder>() {
    class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        var name=itemView.findViewById<TextView>(R.id.clientName)
        var date=itemView.findViewById<TextView>(R.id.date)
        var mobile_number=itemView.findViewById<TextView>(R.id.mobile_number)
        var place=itemView.findViewById<TextView>(R.id.place)
        var moneyGetWay=itemView.findViewById<TextView>(R.id.moneyGetWay)
        var items=itemView.findViewById<TextView>(R.id.items)
        var rest=itemView.findViewById<TextView>(R.id.rest)
        var moneyGet=itemView.findViewById<TextView>(R.id.moneyGet)
        var price=itemView.findViewById<TextView>(R.id.price)
        var fullName=itemView.findViewById<TextView>(R.id.fullName)
        var order_date=itemView.findViewById<TextView>(R.id.order_date)
        var horizontalView1=itemView.findViewById<HorizontalScrollView>(R.id.horizontalView1)
        var exp=itemView.findViewById<ExpansionHeader>(R.id.exp)
        var removeRl=itemView.findViewById<RelativeLayout>(R.id.removeRl)
        var rank=itemView.findViewById<TextView>(R.id.rank)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var v=LayoutInflater.from(_ctx.context).inflate(R.layout.table_item,parent,false)
        return ViewHolder(v)
    }

    override fun getItemCount()=items.size

    var alertDialog: AlertDialog?=null

    fun showDialog(item: OrderData,position: Int){
            alertDialog!!.show()
            alertDialog!!.window!!.setBackgroundDrawable(null)
            val cancel_btn=alertDialog!!.findViewById<Button>(R.id.negative_button_btn)
            cancel_btn.setOnClickListener{
                alertDialog!!.dismiss()
            }
            val postive_btn=alertDialog!!.findViewById<Button>(R.id.postive_btn)
            val order_edittext=alertDialog!!.findViewById<EditText>(R.id.order_edittext)
            postive_btn.setOnClickListener{

                when(item.typeID){
                    1->{
                        orderModel.removeTheater(item.id)

                    }
                    2->{
                        orderModel.removeScreen(item.id)
                    }
                    3->{
                        orderModel.removePhoto(item.id)
                    }
                }
                items.removeAt(position)
                notifyDataSetChanged()
                alertDialog!!.dismiss()
            }



    }
    var orderModel=OrdersViewModel()
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val promptsView: View = LayoutInflater.from(_ctx.context).inflate(R.layout.dialog_view, null)
        val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(
            _ctx.context
        )
        alertDialogBuilder.setView(promptsView)
        alertDialog = alertDialogBuilder.create()


        orderModel= ViewModelProviders.of(_ctx).get(OrdersViewModel::class.java)

        var listItem=items[position]
        holder.date.text=listItem.date
        holder.fullName.text=listItem.clientName
        holder.name.text=listItem.clientName
        holder.mobile_number.text=listItem.phoneNumber
        holder.moneyGet.text=listItem.moneyHave
        holder.moneyGetWay.text=listItem.moneyGet
        holder.rest.text=listItem.moneyRest
        if(listItem.moneyRest.isNotEmpty()&&listItem.moneyHave.isNotEmpty())
          holder.price.text=(listItem.moneyRest.toDouble()+listItem.moneyHave.toDouble()).toString()

        holder.order_date.text=listItem.date
        holder.items.text   =listItem.items
        holder.rank.text=(position+1).toString()
        holder.horizontalView1.postDelayed(({
            holder.horizontalView1.fullScroll(View.FOCUS_RIGHT)
        }),10)
        holder.place.text=listItem.location
//        if(position%2==0)
//            holder.exp.setBackgroundColor(Color.parseColor("#dfe6e9"))
        if(removeBoolean)
            holder.removeRl.visibility=View.VISIBLE
        else
            holder.removeRl.visibility=View.GONE
        holder.removeRl.setOnClickListener{
            showDialog(listItem,position)
        }
    }
}