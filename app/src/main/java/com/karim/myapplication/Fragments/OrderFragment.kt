package com.karim.myapplication.Fragments

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
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
import com.karim.myapplication.Adapter.OrderAdapter
import com.karim.myapplication.Excel.ArchiveExcel
import com.karim.myapplication.Interfaces.OnOrdersDataLoaderLisenter
import com.karim.myapplication.R
import com.karim.myapplication.ViewModel.OrdersViewModel
import com.karim.myapplication.model.OrderData
import com.karim.myapplication.model.TheaterUploadData
import kotlinx.android.synthetic.main.fragment_order.view.*
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
    var btnFilter:ImageView?=null
    var no_order:TextView?=null
    var orderData=ArrayList<OrderData>()
    var hScrool:HorizontalScrollView?=null
    private fun filter(month: String, year: String) {
        var filterlist=ArrayList<OrderData>()
        for(order in orderData){
            val fdate=order.date.split("-")
            val d_date=fdate[1].split("/")
            val d_month=d_date[1]
            val d_year=d_date[2]
            if(month.toInt()==d_month.toInt()&&d_year.toInt()==year.toInt()) filterlist.add(order)
        }
        adapter= OrderAdapter(this,filterlist,true)
        view!!.orderNumber.text = " ${filterlist.size} طلب "
        if(filterlist.size==0)
            Toast.makeText(context,"لاتوجد نتائج",Toast.LENGTH_SHORT).show()
        order_rv!!.adapter=adapter
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_order, container, false)
        hScrool=v!!.findViewById(R.id.horizontalView1)
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
        for(x in 1 until 13 step 1)monthList.add(x.toString())
        val index=monthList.indexOf(currentMonth())
        val monthAdapter=ArrayAdapter(context!!,R.layout.spinner_text_view,monthList)
        monthSpinner!!.adapter=monthAdapter
        monthSpinner!!.setSelection(index)
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
        for (y in 2020 until 2040 step 1)yearList.add(y.toString())
        val yearAdapter=ArrayAdapter(context!!,R.layout.spinner_text_view,yearList)
        yearSpinner.adapter=yearAdapter
        val yearIndex=yearList.indexOf(currentYear())
        yearSpinner.setSelection(yearIndex)
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
            val  excel= ArchiveExcel(sort(orderData),context!!,"حجوزات")
            excel.createExcel()
        }
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
                val sdf3 =
                    SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH)
                try {
                    dt1 = sdf3.parse(item.time)
                    dt2=sdf3.parse(item2.time)
                    if(dt1 < dt2){
                        val temp = list[i]
                        list[i] = list[i+1]
                        list[i + 1] = temp

                        swap = true
                    }
                } catch (e: Exception) {
                    makeToast("حدث خطأ")
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
                    for(i in item.items.indices){
                        val pic=item.items[i]
                        items +=if(i>item.items.size-1)
                            pic.name+"\n"
                        else
                            pic.name
                    }
                    var order=OrderData(3,items,item.clientName,
                        item.moneyGet,item.phoneNumber,item.location,
                        item.date,item.moneyRest,item.moneyHave,item.id,item.workName,item.time,item.checked)
                    orderData.add(order)
                }
                view!!.orderNumber.text="  ${orderData.size} حجز "
                orderData=sort(orderData)
            //    adapter!!.notifyDataSetChanged()
                order_rv!!.adapter=adapter
                currentFilter()
                scrool_right()
            }
        )
    }

    private fun getDateFromString(date: String): Date? {
        val sdate=date.split("-").toTypedArray()
        var testDate=sdate[1]
        if(!testDate.contains("/"))
            testDate=sdate[0]
        val foramt="dd/MM/yyyy"
        val formatter=SimpleDateFormat(foramt,Locale("ar"))
        return formatter.parse(testDate)
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
                var i=0
                for(item in orderModel.screenList.value!!){
                    var str=""
                    for(screen in item.theaterList){
                        str+=" شاشه :"
                        str+= screen.totalMeter.toDouble().toInt().toString()
                        str+=" متر "
                        str+="\n"
                    }
                    str=str.removeSuffix("\n")
                    var order=OrderData(2,str,item.clientName,
                        item.moneyGet,item.phoneNumber,item.location,
                        item.date,item.moneyRest.toDouble().toInt().toString()
                        ,item.moneyHave.toDouble().toInt().toString(),item.id,item.workName,item.time,item.checked)
                    orderData.add(order)
                }
                view!!.orderNumber.text="${orderData.size} حجز "
                orderData=sort(orderData)
               // adapter!!.notifyDataSetChanged()
                order_rv!!.adapter=adapter
                currentFilter()
                scrool_right()

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
                            str+=" مسرح :"
                            str+= screen.totalMeter.toDouble().toInt().toString()
                            str+=" متر "
                            str+="\n"
                        }
                    str=str.removeSuffix("\n")
                    var order=OrderData(1,str,item.clientName,
                        item.moneyGet,item.phoneNumber,item.location,
                        item.date,item.moneyRest.toDouble().toInt().toString()
                        ,item.moneyHave.toDouble().toInt().toString()
                        ,item.id,item.workName,item.time,item.checked)
                    orderData.add(order)
                }
                orderData=sort(orderData)
                view!!.orderNumber.text=" ${orderData.size} حجز "
               // adapter!!.notifyDataSetChanged()
                order_rv!!.adapter=adapter
                currentFilter()
                scrool_right()
            }
        )
    }

    private fun scrool_right() {
       hScrool!!.postDelayed(({
            v!!.horizontalView1.fullScroll(HorizontalScrollView.FOCUS_RIGHT)
        }), 100)
    }

    private fun currentMonth():String{
        val date = Calendar.getInstance().time
        val month_val = SimpleDateFormat("MM").format(date)
        return  month_val.toInt().toString()
    }
    private fun currentYear():String{
        val date = Calendar.getInstance().time
        val year_val = SimpleDateFormat("yyyy").format(date)
        return  year_val.toInt().toString()
    }
    private fun currentFilter() {
        filter(currentMonth(), currentYear())
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
