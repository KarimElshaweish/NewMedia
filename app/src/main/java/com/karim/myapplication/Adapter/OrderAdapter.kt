package com.karim.myapplication.Adapter

import android.app.AlertDialog
import android.content.Intent
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.karim.myapplication.Activites.ViewContractActivity
import com.karim.myapplication.Converter.NumberConverter.Companion.arabNumber
import com.karim.myapplication.R
import com.karim.myapplication.Util
import com.karim.myapplication.ViewModel.MonthilyAllOrderViewModel
import com.karim.myapplication.ViewModel.OrdersViewModel
import com.karim.myapplication.model.BasketData
import com.karim.myapplication.model.OrderData
import kotlinx.android.synthetic.main.activity_view_contract.*
import java.util.*

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
        var priceTitle=itemView.findViewById<TextView>(R.id.priceTitle)
        var nameTitle=itemView.findViewById<TextView>(R.id.clientNameTitle)
        var dateTitle=itemView.findViewById<TextView>(R.id.dateTitle)
        var mobile_number_Title=itemView.findViewById<TextView>(R.id.phoneNumberTitle)
        var placeTitle=itemView.findViewById<TextView>(R.id.placeTitle)
        var moneyGetWayTitle=itemView.findViewById<TextView>(R.id.wayTitle)
        var itemsTitle=itemView.findViewById<TextView>(R.id.itemsTitle)
        var restTitle=itemView.findViewById<TextView>(R.id.restTitle)
        var moneyGetTitle=itemView.findViewById<TextView>(R.id.getTitle)
        var fullName=itemView.findViewById<TextView>(R.id.fullName)
        var order_date=itemView.findViewById<TextView>(R.id.order_date)
        var removeRl=itemView.findViewById<RelativeLayout>(R.id.removeRl)
        var spliter=itemView.findViewById<View>(R.id.spliter)
        var dealContractBtn=itemView.findViewById<ImageView>(R.id.dealContractBtn)
        var statusCheckBox=itemView.findViewById<CheckBox>(R.id.statusCheckBox)
        var contracText=itemView.findViewById<TextView>(R.id.contracText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var v=LayoutInflater.from(_ctx.context).inflate(R.layout.table_item,parent,false)
        return ViewHolder(v)
    }

    override fun getItemCount()=items.size

    var alertDialog: AlertDialog?=null

    fun showDialog(item: OrderData,position: Int,removeBoolean: Boolean){
            alertDialog!!.show()
            alertDialog!!.window!!.setBackgroundDrawable(null)
            val cancel_btn=alertDialog!!.findViewById<Button>(R.id.negative_button_btn)
            cancel_btn.setOnClickListener{
                alertDialog!!.dismiss()
            }
            val postive_btn=alertDialog!!.findViewById<Button>(R.id.postive_btn)
            val order_edittext=alertDialog!!.findViewById<EditText>(R.id.order_edittext)
            postive_btn.setOnClickListener{
                if(removeBoolean)
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
                }else{
                    when(item.typeID){
                        1->{
                            monthModel.removeTheater(item.id)
                        }
                        2->{
                            monthModel.removeScreen(item.id)
                        }
                        3->{
                            monthModel.removePhoto(item.id)
                        }
                        4->{
                            monthModel.removeMusic(item.id)
                        }
                        5->{
                            monthModel.remoMontage(item.id)
                        }
                    }
                }
                items.removeAt(position)
                notifyDataSetChanged()
                alertDialog!!.dismiss()
            }


    }
    var orderModel=OrdersViewModel()
    var monthModel=MonthilyAllOrderViewModel()
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var locale= Locale("ar")
        Locale.setDefault(locale)
        var config =
            _ctx.resources.configuration
        config.setLocale(locale)
        val promptsView: View = LayoutInflater.from(_ctx.context).inflate(R.layout.dialog_view, null)
        val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(
            _ctx.context
        )
        alertDialogBuilder.setView(promptsView)
        alertDialog = alertDialogBuilder.create()


        orderModel= ViewModelProviders.of(_ctx).get(OrdersViewModel::class.java)
        monthModel=ViewModelProviders.of(_ctx).get(MonthilyAllOrderViewModel::class.java)
        var listItem=items[position]
        holder.date.text= arabNumber(listItem.date)
        holder.fullName.text=listItem.clientName
        holder.name.text=listItem.clientName
        holder.mobile_number.text=arabNumber(listItem.phoneNumber)
        holder.moneyGet.text=arabNumber(listItem.moneyHave)
        holder.moneyGetWay.text=listItem.moneyGet
        holder.rest.text=arabNumber(listItem.moneyRest)
        if(listItem.moneyRest.isNotEmpty()&&listItem.moneyHave.isNotEmpty())
          holder.price.text=
              arabNumber((listItem.moneyRest.toDouble()+listItem.moneyHave.toDouble()).toInt().toString())

        holder.order_date.text=arabNumber(listItem.date)
        holder.items.text=listItem.items

        holder.place.text=listItem.location
//        if(position%2==0)
//            holder.exp.setBackgroundColor(Color.parseColor("#dfe6e9"))
            holder.removeRl.visibility=View.VISIBLE
        holder.removeRl.setOnClickListener{
            showDialog(listItem,position,removeBoolean)
        }
        if(position==0){
            holder.dateTitle.visibility=View.VISIBLE
            holder.itemsTitle.visibility=View.VISIBLE
            holder.mobile_number_Title.visibility=View.VISIBLE
            holder.moneyGetTitle.visibility=View.VISIBLE
            holder.nameTitle.visibility=View.VISIBLE
            holder.priceTitle.visibility=View.VISIBLE
            holder.placeTitle.visibility=View.VISIBLE
            holder.moneyGetWayTitle.visibility=View.VISIBLE
            holder.restTitle.visibility=View.VISIBLE
            holder.contracText.visibility=View.VISIBLE
            holder.spliter.visibility=View.GONE
        }else{
            holder.dateTitle.visibility=View.GONE
            holder.itemsTitle.visibility=View.GONE
            holder.mobile_number_Title.visibility=View.GONE
            holder.moneyGetTitle.visibility=View.GONE
            holder.nameTitle.visibility=View.GONE
            holder.priceTitle.visibility=View.GONE
            holder.placeTitle.visibility=View.GONE
            holder.moneyGetWayTitle.visibility=View.GONE
            holder.restTitle.visibility=View.GONE
            holder.contracText.visibility=View.GONE
            holder.spliter.visibility=View.VISIBLE
        }
        holder.dealContractBtn.setOnClickListener{
            val intent=Intent(_ctx.context,ViewContractActivity::class.java)
            intent.putExtra("id",listItem.typeID)
            intent.putExtra("orderID",listItem.id)
            Util.orderDataObject=listItem
            _ctx.startActivity(intent)
        }
        if(removeBoolean) {
            holder.statusCheckBox.visibility = View.GONE
        }
        holder.statusCheckBox.isChecked = items[position].checked
        holder.statusCheckBox.setOnCheckedChangeListener {
            buttonView, isChecked ->
            when(items[position].typeID){
                1->{
                    monthModel.updateTheaterTheater(items[position].id,isChecked)
                }
                2->{
                    monthModel.updateScreen(items[position].id,isChecked)
                }
                3->{
                  //  monthModel.removePhoto(item.id)
                    monthModel.updatePhoto(items[position].id,isChecked)

                }
                4->{
                //    monthModel.removeMusic(item.id)
                    monthModel.updateMusic(items[position].id,isChecked)
                }
                5->{
                  //  monthModel.remoMontage(item.id)
                    monthModel.updateMontage(items[position].id,isChecked)

                }
            }
        }
    }
}