package com.karim.myapplication.Fragments
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.AppCompatSpinner
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.karim.myapplication.Adapter.OrderAdapter
import com.karim.myapplication.Excel.ArchiveExcel
import com.karim.myapplication.Interfaces.OnMonthilyOrderLoad
import com.karim.myapplication.Print.PrintPDF
import com.karim.myapplication.R
import com.karim.myapplication.ViewModel.MonthilyAllOrderViewModel
import com.karim.myapplication.model.OrderData
import kotlinx.android.synthetic.main.fragment_order.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class PastOrderFragment : Fragment(), OnMonthilyOrderLoad {

    var v:View?=null
    var arch_rv:RecyclerView?=null
    var adapter: OrderAdapter?=null
    var orderData=ArrayList<OrderData>()
    var model: MonthilyAllOrderViewModel? = null
    var monthSpinner:AppCompatSpinner?=null
    var month="-1"
    var year="-1"
    var btnFilter:Button?=null
    var no_order:TextView?=null
    var monthBolean=false
    var yearBolean=false
    var arch_scroll:ScrollView?=null
    var print_pdf_arch:Button?=null
    var print:PrintPDF?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       v= inflater.inflate(R.layout.fragment_past_order, container, false)
        print= PrintPDF(context!!)
        print_pdf_arch=v!!.findViewById(R.id.print_pdf_arch)
        print_pdf_arch!!.setOnClickListener{
            if(adapter!!.itemCount>0){
               // print!!.createPdf(arch_scroll!!,arch_scroll!!.getChildAt(0).height,arch_scroll!!.width)
                val  excel=ArchiveExcel(orderData,context!!,"طلبات")
                excel.createExcel()
            }
        }
        arch_scroll=v!!.findViewById(R.id.arch_scroll)
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
        model = ViewModelProviders.of(this).get(MonthilyAllOrderViewModel::class.java)
        model!!.init(this)
        arch_rv=v!!.findViewById(R.id.arch_rv)
        arch_rv!!.setHasFixedSize(true)
        arch_rv!!.layoutManager=LinearLayoutManager(context)
        adapter= OrderAdapter(this, orderData,false)
        arch_rv!!.adapter=adapter
        return v
    }

    @SuppressLint("SetTextI18n")
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
          Toast.makeText(context,getString(R.string.no_order_date),Toast.LENGTH_SHORT).show()
        arch_rv!!.adapter=adapter
    }

    private fun getDateFromString(date: String): Date? {
        val sdate=date.split("-").toTypedArray()
        var testDate=sdate[1]
        if(!testDate.contains("/"))
            testDate=sdate[0]
        val foramt="dd/MM/yyyy"
        val formatter= SimpleDateFormat(foramt,Locale("ar"))
        return formatter.parse(testDate)
    }
   private  fun sort(list:ArrayList<OrderData>):ArrayList<OrderData>{
        var swap = true
        while(swap){
            swap = false
            for(i in 0 until list.size-1){
                var item=list[i]
                var item2=list[i+1]
                var dt1: Date?
                var dt2: Date?
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

    override fun onPicutersOrderLoad() {
        model!!.photList.observe(
            this,Observer<Any>{
                view!!.screenPb.visibility=View.GONE
                for(item in model!!.photList.value!!){
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
                view!!.orderNumber.text="  ${orderData.size} طلب "
                orderData=sort(orderData)
                adapter!!.notifyDataSetChanged()
                arch_rv!!.adapter=adapter
            }
        )

    }

    override fun onTheaterloadedSuccess() {
        model!!.theaterList.observe(
            this, Observer<Any> {
               view!!.screenPb.visibility = View.GONE
                for (item in model!!.theaterList.value!!) {
                    var str = ""
                    for (screen in item.theaterList) {
                        str += " عدد الامتار :"
                        str += screen.totalMeter
                        str += "-"
                        str += " السعر :"
                        str += screen.price
                        str+="\n"
                    }
                    var order = OrderData(
                        1, str, item.clientName,
                        item.moneyGet, item.phoneNumber, item.location,
                        item.date, item.moneyRest, item.moneyHave, item.id, item.workName
                    )
                    orderData.add(order)
                }
                orderData = sort(orderData)
                view!!.orderNumber.text = " ${orderData.size} طلب "
                adapter!!.notifyDataSetChanged()
                arch_rv!!.adapter=adapter
            })
    }

    override fun onScreenloadedSuceess() {
        model!!.screenList.observe(
            this, Observer<Any>{
                view!!.screenPb.visibility=View.GONE
                for(item in model!!.screenList.value!!){
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
                view!!.orderNumber.text="${orderData.size} طلب "
                orderData=sort(orderData)
                adapter!!.notifyDataSetChanged()
                arch_rv!!.adapter=adapter

            }
        )


    }

    override fun onScreenloadedFailed() {
    }

    override fun onTheaterloadedFailed() {
    }

    override fun onPictureFailed() {
    }

    @SuppressLint("SetTextI18n")
    override fun onMusicOrderLoad() {
        model!!.musicList.observe(
            this,Observer<Any>{
                view!!.screenPb.visibility=View.GONE
                for(item in model!!.musicList.value!!){
                    var items=""
                    for(misc in item.items){
                        if (misc.items!=null)
                        for(misc1 in misc.items)
                            items +=misc1.item+"-"
                    }
                    val order=OrderData(3,items,item.clientName,
                        item.moneyGet,item.phoneNumber,item.location,
                        item.date,item.moneyRest,item.moneyHave,item.id,item.workName)
                    orderData.add(order)
                }
                view!!.orderNumber.text="  ${orderData.size} طلب "
                orderData=sort(orderData)
                adapter!!.notifyDataSetChanged()
                arch_rv!!.adapter=adapter
            }
        )
    }

    override fun onMusicOrderLoadFailed() {
    }

    @SuppressLint("SetTextI18n")
    override fun onMontagLoaded() {
        model!!.montagList.observe(
            this,Observer<Any>{
                view!!.screenPb.visibility=View.GONE
                for(item in model!!.montagList.value!!){
                    val order=OrderData(5,"مونتاج",item.name,
                        item.cashGet,item.phoneNumber,"",
                        item.date,item.cashRest,"",item.id,"")
                    orderData.add(order)
                }
                view!!.orderNumber.text="  ${orderData.size} طلب "
                orderData=sort(orderData)
                adapter!!.notifyDataSetChanged()
                arch_rv!!.adapter=adapter
            }
        )
    }

    override fun onMontageFaield() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
