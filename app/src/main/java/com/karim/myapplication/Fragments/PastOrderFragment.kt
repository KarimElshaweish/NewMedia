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


class PastOrderFragment : Fragment(), OnMonthilyOrderLoad{

    var v:View?=null
    var arch_rv:RecyclerView?=null
    var adapter: OrderAdapter?=null
    var orderData=ArrayList<OrderData>()
    var model: MonthilyAllOrderViewModel? = null
    var monthSpinner:AppCompatSpinner?=null
    var month="-1"
    var year="-1"
    var btnFilter:ImageView?=null
    var no_order:TextView?=null
    var print_pdf_arch:Button?=null
    var print:PrintPDF?=null
    var hScrool:HorizontalScrollView?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       v= inflater.inflate(R.layout.fragment_past_order, container, false)
        hScrool=v!!.findViewById(R.id.horizontalView1)
        print= PrintPDF(context!!)
        print_pdf_arch=v!!.findViewById(R.id.print_pdf_arch)
        print_pdf_arch!!.setOnClickListener{
            if(adapter!!.itemCount>0){
                val  excel=ArchiveExcel(sort(orderData),context!!,"طلبات")
                excel.createExcel()
            }
        }
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
        yearList.add(getString(R.string.year))
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
        model = ViewModelProviders.of(this).get(MonthilyAllOrderViewModel::class.java)
        model!!.init(this)
        arch_rv=v!!.findViewById(R.id.arch_rv)
        arch_rv!!.setHasFixedSize(true)
        arch_rv!!.layoutManager=LinearLayoutManager(context)
        adapter= OrderAdapter(this, orderData,false)
        arch_rv!!.adapter=adapter
        return v
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
             //   dt1 = item.time as Date//getDateFromString((item).date)
           //     dt2 = item2.time as Date//getDateFromString(item2.date)

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
                    for(i in item.items.indices){
                        val pic=item.items[i]
                        items += if(i>item.items.size-1)
                            pic.name+"\n"
                        else
                            pic.name
                    }
                    var order=OrderData(3,items,item.clientName,
                        item.moneyGet,item.phoneNumber,item.location,
                        item.date,item.moneyRest.toDouble().toInt().toString(),item.moneyHave.toDouble().toInt().toString(),item.id,item.workName,item.time,item.checked)
                    orderData.add(order)
                }
                view!!.orderNumber.text="  ${orderData.size} طلب "
                orderData=sort(orderData)
                //adapter!!.notifyDataSetChanged()
                arch_rv!!.adapter=adapter
                scrool_right()
                currentFilter()
            }
        )

    } //2

    override fun onTheaterloadedSuccess() {
        model!!.theaterList.observe(
            this, Observer<Any> {
               view!!.screenPb.visibility = View.GONE
                for (item in model!!.theaterList.value!!) {
                    var str = ""
                    for (screen in item.theaterList) {
                        str += " مسرح :"
                        str += screen.totalMeter.toDouble().toInt().toString()
                        str += " متر "
                        str+="\n"
                    }
                    str=str.removeSuffix("\n")
                    var order = OrderData(
                        1, str, item.clientName,
                        item.moneyGet, item.phoneNumber, item.location,
                        item.date, item.moneyRest.toDouble().toInt().toString(),
                        item.moneyHave.toDouble().toInt().toString(), item.id, item.workName,
                        item.time
                    ,item.checked)
                    orderData.add(order)
                }
                orderData = sort(orderData)
                view!!.orderNumber.text = " ${orderData.size} طلب "
                //adapter!!.notifyDataSetChanged()
                arch_rv!!.adapter=adapter
                scrool_right()
                currentFilter()
            })
    } //3

    override fun onScreenloadedSuceess() {
        model!!.screenList.observe(
            this, Observer<Any>{
                view!!.screenPb.visibility=View.GONE
                for(item in model!!.screenList.value!!){
                    var str=""
                    for(screen in item.theaterList){
                        str+=" شاشة :"
                        str+= screen.totalMeter.toDouble().toInt().toString()
                        str+=" متر "
                        str+="\n"
                    }
                    str.removeSuffix("\n")
                    var order=OrderData(2,str,item.clientName,
                        item.moneyGet,item.phoneNumber,item.location,
                        item.date,item.moneyRest.toDouble().toInt().toString(),item.moneyHave
                            .toDouble().toInt().toString(),item.id,item.workName,
                        item.time,item.checked)
                    orderData.add(order)
                }
                view!!.orderNumber.text="${orderData.size} طلب "
                orderData=sort(orderData)
                //adapter!!.notifyDataSetChanged()
                arch_rv!!.adapter=adapter
                scrool_right()
                currentFilter()
            }
        )


    } //4

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
                    for(i in item.items.indices){
                        val misc=item.items[i]
                        if (misc.items!=null)
                            items +=
                                if(i>item.items.size-1)
                                misc.name+"\n"
                                else
                                misc.name
                    }
                    items=items.removeRange(items.length-2,items.length-1)
                    val order=OrderData(4,items,item.clientName,
                        item.moneyGet,item.phoneNumber,item.location,
                        item.date,item.moneyRest.toDouble().toInt().toString()
                        ,item.moneyHave.toDouble().toInt().toString(),item.id,item.workName,
                        item.time,item.checked)
                    orderData.add(order)
                }
                view!!.orderNumber.text="  ${orderData.size} طلب "
                orderData=sort(orderData)
               // adapter!!.notifyDataSetChanged()
                arch_rv!!.adapter=adapter
                scrool_right()
                currentFilter()
            }
        )
    }   //1

    override fun onMusicOrderLoadFailed() {
    }

    @SuppressLint("SetTextI18n")
    override fun onMontagLoaded() {
        model!!.montagList.observe(
            this,Observer<Any>{
                view!!.screenPb.visibility=View.GONE
                for(item in model!!.montagList.value!!){
                    val order=OrderData(5,"مونتاج",item.name,
                      "",item.phoneNumber,"",
                        item.date,item.cashRest.toDouble().toInt().toString()
                        ,  item.cashGet.toDouble().toInt().toString(),item.id,"",item.time,item.checked)
                    orderData.add(order)
                }
                view!!.orderNumber.text="  ${orderData.size} طلب "
                orderData=sort(orderData)
               // adapter!!.notifyDataSetChanged()
                arch_rv!!.adapter=adapter
                scrool_right()
                currentFilter()
            }
        )
    } //5

    override fun onMontageFaield() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    private fun scrool_right() {
        hScrool!!.postDelayed(({
            v!!.horizontalView1.fullScroll(HorizontalScrollView.FOCUS_RIGHT)
        }), 100)
    }
    fun makeToast(msg:String){
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show()
    }
    override fun onphotoRemoveSuccess() {
        view!!.orderNumber.text=" ${adapter!!.itemCount} حجز "
        arch_rv!!.adapter=adapter
        makeToast(getString(R.string.orde_removed))
    }

    override fun onPhotoRemoveFailed() {
        makeToast(getString(R.string.cannot_removed_photo))
    }

    override fun onTheaterRemovedSuccess() {
        makeToast(getString(R.string.theater_removed_successfully))
        view!!.orderNumber.text=" ${adapter!!.itemCount} حجز "
        arch_rv!!.adapter=adapter
    }

    override fun onTheaterRemovedFailed() {
        makeToast(getString(R.string.cannot_delete_theater))
    }
    override fun onScreenRemovedSuccess() {
        arch_rv!!.adapter=adapter
        makeToast(getString(R.string.theater_removed_successfully))
        view!!.orderNumber.text=" ${adapter!!.itemCount} حجز "
    }
    override fun onScreenRemovedFailed() {
        makeToast(getString(R.string.cannot_delete))
    }

    override fun onMusicRemovedSuccessfully() {
        arch_rv!!.adapter=adapter
        makeToast(getString(R.string.sound_removed))
        view!!.orderNumber.text=" ${adapter!!.itemCount} حجز "
    }

    override fun onMusicRemovedFailed() {
        makeToast(getString(R.string.cannot_delete))
    }

    override fun onMontagRemovedSuccessfully() {
        arch_rv!!.adapter=adapter
        makeToast(getString(R.string.montage_delete))
        view!!.orderNumber.text=" ${adapter!!.itemCount} حجز "
    }

    override fun onMontagRemovedFailed() {
        makeToast(getString(R.string.cannot_delete))
    }

}
