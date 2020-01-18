package com.karim.myapplication.Fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.graphics.*
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatSpinner
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.karim.myapplication.Adapter.OrderAdapter
import com.karim.myapplication.Excel.ArchiveExcel
import com.karim.myapplication.Interfaces.OnOrdersDataLoaderLisenter
import com.karim.myapplication.R
import com.karim.myapplication.ViewModel.OrdersViewModel
import com.karim.myapplication.model.OrderData
import com.karim.myapplication.model.ScreenUploadData
import com.karim.myapplication.model.TheaterUploadData
import com.karim.myapplication.model.photoData
import kotlinx.android.synthetic.main.fragment_order.*
import kotlinx.android.synthetic.main.fragment_order.view.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class OrderFragment : Fragment(), OnOrdersDataLoaderLisenter {
    var orderModel=OrdersViewModel()
    var v: View? = null
    var previous:TextView?=null
    var next:TextView?=null
    var order_rv:RecyclerView?=null
    var adapter:OrderAdapter?=null
    var monthSpinner: AppCompatSpinner?=null
    var month="-1"
    var year="-1"
    var btnFilter:Button?=null
    var no_order:TextView?=null
    var monthBolean=false
    var yearBolean=false
    var orderData=ArrayList<OrderData>()
    private fun filter(month: String, year: String) {
        var filterlist=ArrayList<OrderData>()
        for(order in orderData){
            val fdate=order.date.split("-")
            val d_date=fdate[1].split("/")
            val d_month=d_date[1]
            val d_year=d_date[2]
            if(month.toInt()==d_month.toInt()&&d_year.toInt()==year.toInt()) filterlist.add(order)
        }
        adapter= OrderAdapter(this,filterlist,false)
        view!!.orderNumber.text = " ${filterlist.size} طلب "
        if(filterlist.size==0)
            no_order!!.visibility=View.VISIBLE
        order_rv!!.adapter=adapter
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_order, container, false)
        order_rv=v!!.findViewById(R.id.order_rv)
        order_rv!!.setHasFixedSize(true)
        order_rv!!.layoutManager=LinearLayoutManager(context)
        no_order=v!!.findViewById(R.id.no_order)
        btnFilter=v!!.findViewById(R.id.btnFilter)
        btnFilter!!.setOnClickListener{
            if(month != "-1" && year != "-1"&&month!=getString(R.string.month)&&year!=getString(R.string.year)){
                filter(month,year)
            }else{
                Toast.makeText(context!!,"من فضلك اختار السنه والشهر",Toast.LENGTH_SHORT).show()
            }
        }
        monthSpinner=v!!.findViewById(R.id.monthSpinner)
        var monthList= mutableListOf<String>()
        monthList.add(getString(R.string.month))
        for(x in 1 until 13 step 1)monthList.add(x.toString())
        val monthAdapter=ArrayAdapter(context!!,R.layout.spinner_text_view,monthList)
        monthSpinner!!.adapter=monthAdapter
        monthSpinner!!.onItemSelectedListener=object :AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                month=monthList[position]
            }

        }
        val yearSpinner=v!!.findViewById<AppCompatSpinner>(R.id.yearSpinner)
        var yearList= mutableListOf<String>()
        yearList.add(getString(R.string.year))
        for (y in 2020 until 2040 step 1)yearList.add(y.toString())
        val yearAdapter=ArrayAdapter(context!!,R.layout.spinner_text_view,yearList)
        yearSpinner.adapter=yearAdapter
        yearSpinner!!.onItemSelectedListener=object :AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                year=yearList[position]
            }

        }
        val print_pdf_order=v!!.findViewById<Button>(R.id.print_pdf_order)
        print_pdf_order.setOnClickListener{
            val  excel= ArchiveExcel(orderData,context!!,"حجوزات")
            excel.createExcel()
        }

        v!!.horizontalView1.postDelayed(({
            v!!.horizontalView1.fullScroll(View.FOCUS_RIGHT)
        }),10)
        orderModel=ViewModelProviders.of(this).get(OrdersViewModel::class.java)
        orderModel.init(this)
        orderModel.getPhoto()
        orderModel.getTheater()
        adapter= OrderAdapter(this, orderData,true)
        order_rv!!.adapter=adapter
        return v
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun sort(list:ArrayList<OrderData>):ArrayList<OrderData>{
        var swap = true
        while(swap){
            swap = false
            for(i in 0 until list.size-1){
                var item=list[i]
                var item2=list[i+1]
                var dt1:Date?
                var dt2:Date?
                dt1 = getDateFromString((item).date)
                dt2 = getDateFromString(item2.date)
                if(dt1!! > dt2!!){
                    val temp = list[i]
                    list[i] = list[i+1]
                    list[i + 1] = temp

                    swap = true
                }
            }
        }
        return list
    }
    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onPicutersOrderLoad() {
        orderModel.photList.observe(
            this,Observer<Any>{
                view!!.screenPb.visibility=View.GONE
                for(item in orderModel.photList.value!!){
                    var items=""
                    for(pic in item.items){
                        for(pic1 in pic.items)
                        items +=pic1.item+"-"
                    }
                    var order=OrderData(3,items,item.clientName,
                        item.moneyGet,item.phoneNumber,item.location,
                        item.date,item.moneyRest,item.moneyHave,item.id,item.workName)
                    orderData.add(order)
                }
                view!!.orderNumber.text="  ${orderData.size} حجز "
                orderData=sort(orderData)
                adapter!!.notifyDataSetChanged()
            }
        )
    }

    private fun getDateFromString(date: String): Date? {
        var foramt="EEEE- dd/MM/yyyy"
        val formatter=SimpleDateFormat(foramt,Locale("ar"))
        return formatter.parse(date)
    }

    override fun onMusicOrderLoad() {
//        orderModel.soundList.observe(
//            this, Observer<Any>{
//                for(work in orderModel.soundList.value!!){
//                    createTableRow(work,v!!)
//                    dialog!!.dismiss()
//                }
//            }
//        )
//        orderModel.getSound()
    }

    override fun onPictureFailed() {
        orderModel.getSound()
        Toast.makeText(context,"حدث خطأ",Toast.LENGTH_SHORT).show()
    }

    override fun onMusicFailed() {
        Toast.makeText(context,"حدث خطأ",Toast.LENGTH_SHORT).show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onScreenloadedSuceess() {
        orderModel.screenList.observe(
            this, Observer<Any>{
                view!!.screenPb.visibility=View.GONE
                for(item in orderModel.screenList.value!!){
                    var str=""
                    for(screen in item.theaterList){
                        str+=" عدد الامتار :"
                        str+= screen.totalMeter
                        str+="-"
                        str+=" السعر :"
                        str+=screen.price
                        str+="\n"
                    }
                    var order=OrderData(2,str,item.clientName,
                        item.moneyGet,item.phoneNumber,item.location,
                        item.date,item.moneyRest,item.moneyHave,item.id,item.workName)
                    orderData.add(order)
                }
                view!!.orderNumber.text="${orderData.size} حجز "
                orderData=sort(orderData)
                adapter!!.notifyDataSetChanged()

            }
        )
    }

    override fun onScreenloadedFailed() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    var thaeterList=ArrayList<TheaterUploadData>()
    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onTheaterloadedSuccess() {
        orderModel.theaterList.observe(
            this, Observer <Any>{
                view!!.screenPb.visibility=View.GONE
                for(item in orderModel.theaterList.value!!){
                        var str=""
                        for(screen in item.theaterList){
                            str+=" عدد الامتار :"
                            str+= screen.totalMeter
                            str+="-"
                            str+=" السعر :"
                            str+=screen.price
                            str+="\n"
                        }
                    var order=OrderData(1,str,item.clientName,
                        item.moneyGet,item.phoneNumber,item.location,
                        item.date,item.moneyRest,item.moneyHave,item.id,item.workName)
                    orderData.add(order)
                }
                orderData=sort(orderData)
                view!!.orderNumber.text=" ${orderData.size} حجز "
                adapter!!.notifyDataSetChanged()
            }
        )
    }

    override fun onTheaterloadedFailed() {
        makeToast(getString(R.string.cannot_delete))
    }

    override fun onphotoRemoveSuccess() {
        view!!.orderNumber.text=" ${adapter!!.itemCount} حجز "
        view!!.order_rv.adapter=adapter
        makeToast(getString(R.string.orde_removed))
    }

    override fun onPhotoRemoveFailed() {
        makeToast(getString(R.string.cannot_removed_photo))
    }

    override fun onTheaterRemovedSuccess() {
        makeToast(getString(R.string.theater_removed_successfully))
        view!!.orderNumber.text=" ${adapter!!.itemCount} حجز "
        view!!.order_rv.adapter=adapter
    }

    override fun onTheaterRemovedFailed() {
        makeToast(getString(R.string.cannot_delete_theater))
    }
    fun makeToast(msg:String){
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show()
    }
    override fun onScreenRemovedSuccess() {
        view!!.order_rv.adapter=adapter
        makeToast(getString(R.string.theater_removed_successfully))
        view!!.orderNumber.text=" ${adapter!!.itemCount} حجز "
    }
    override fun onScreenRemovedFailed() {
        makeToast(getString(R.string.cannot_delete))
    }
}
