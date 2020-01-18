package com.karim.myapplication.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.AppCompatSpinner
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.karim.myapplication.Adapter.OrderAdapter
import com.karim.myapplication.Interfaces.Report
import com.karim.myapplication.R
import com.karim.myapplication.ViewModel.ReportViewModel
import com.karim.myapplication.model.OrderData
import com.karim.myapplication.model.ReportData
import kotlinx.android.synthetic.main.fragment_order.view.*
import kotlinx.android.synthetic.main.fragment_report.*
import kotlinx.android.synthetic.main.fragment_report.view.*
import kotlinx.android.synthetic.main.sound_report.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ReportFragment : Fragment(),Report {
    var reportModel=ReportViewModel()
    var v:View?=null
    var monthSpinner: AppCompatSpinner?=null
    var month="-1"
    var year="-1"
    var btnFilter: ImageView?=null
    private fun filter(month: String, year: String) {
        v!!.totalDep.text=0.0.toString()
        var str="إيرادات شهر "
        str+=month
        str+=" لسنه "
        str+=year
        v!!.tip_info.text=str
        var price=0.0
        for(report in reportModel.montageReportLsit.value!!){
            val fdate=report.date.split("-")
            val d_date=fdate[1].split("/")
            val d_month=d_date[1]
            val d_year=d_date[2]
            if(month.toInt()==d_month.toInt()&&d_year.toInt()==year.toInt())
                price+=report.price.toDouble()
        }
        v!!.montagDep.text=price.toString()
        updateTotal(price)
        price=0.0
        for(report in reportModel.picuterReportLsit.value!!){
            val fdate=report.date.split("-")
            val d_date=fdate[1].split("/")
            val d_month=d_date[1]
            val d_year=d_date[2]
            if(month.toInt()==d_month.toInt()&&d_year.toInt()==year.toInt())
                price+=report.price.toDouble()
        }
        v!!.photoDep.text=price.toString()
        updateTotal(price)
        price=0.0
        for(report in reportModel.musicReportLsit.value!!){
            val fdate=report.date.split("-")
            val d_date=fdate[1].split("/")
            val d_month=d_date[1]
            val d_year=d_date[2]
            if(month.toInt()==d_month.toInt()&&d_year.toInt()==year.toInt())
                price+=report.price.toDouble()
        }
        v!!.musicDep.text=price.toString()
        updateTotal(price)
        price=0.0
        for(report in reportModel.screenReportLsit.value!!){
            val fdate=report.date.split("-")
            val d_date=fdate[1].split("/")
            val d_month=d_date[1]
            val d_year=d_date[2]
            if(month.toInt()==d_month.toInt()&&d_year.toInt()==year.toInt())
                price+=report.price.toDouble()
        }
        v!!.screenDep.text=price.toString()
        updateTotal(price)
        price=0.0
        for(report in reportModel.theaterReportLsit.value!!){
            val fdate=report.date.split("-")
            val d_date=fdate[1].split("/")
            val d_month=d_date[1]
            val d_year=d_date[2]
            if(month.toInt()==d_month.toInt()&&d_year.toInt()==year.toInt())
                price+=report.price.toDouble()
        }
        v!!.theaterDep.text=price.toString()
        updateTotal(price)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_report, container, false)
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
        val monthAdapter= ArrayAdapter(context!!,R.layout.spinner_text_view,monthList)
        monthSpinner!!.adapter=monthAdapter
        monthSpinner!!.onItemSelectedListener=object : AdapterView.OnItemSelectedListener{
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
        val yearAdapter= ArrayAdapter(context!!,R.layout.spinner_text_view,yearList)
        yearSpinner.adapter=yearAdapter
        yearSpinner!!.onItemSelectedListener=object : AdapterView.OnItemSelectedListener{
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
        reportModel=ViewModelProviders.of(this).get(ReportViewModel::class.java)
        reportModel.init(this)
        return  v
    }
    fun makeToast(msg:String){
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show()
    }
    fun updateTotal(price:Double){
        var totalPrice=v!!.totalDep.text.toString().toDouble()
        totalPrice+=price
        v!!.totalDep.text=totalPrice.toString()
    }
    override fun onReportMontageMoneyLoadedSuccess() {
        reportModel.montageReportLsit.observe(this,
            Observer{
                var price:Double=0.0
                for (item in reportModel.montageReportLsit.value!!){
                    price+=item.price.toDouble()
                }
                v!!.montagDep.text=price.toString()
                updateTotal(price)
            })
    }

    override fun onReportMontageMoneyLoadedFailed() {
        makeToast(getString(R.string.error))
    }

    override fun onReportPhotoMoneyLoadedFaield() {
        makeToast(getString(R.string.error))
    }

    override fun onReportPhotoMoneyLoadedSuccess() {
        reportModel.picuterReportLsit.observe(this,
            Observer {
                var price:Double=0.0
                for(item in reportModel.picuterReportLsit.value!!)
                    price+=item.price.toDouble()
                v!!.photoDep.text=price.toString()
                updateTotal(price)
            })
    }

    override fun onReportMusicMoneyLoadedSuccess() {
        reportModel.musicReportLsit.observe(this,
            Observer {
                var price:Double=0.0
                for(item in  reportModel.musicReportLsit.value!!)
                    price+=item.price.toDouble()
                v!!.musicDep.text=price.toString()
                updateTotal(price)
            })
    }

    override fun onReportMusicMoneyLoadedFailed() {
        makeToast(getString(R.string.error))
    }

    override fun onReportTheaterMoneyLoadedFaield() {
        makeToast(getString(R.string.error))
    }

    override fun onReportTheaterMoneyLoadedSuccess() {
        reportModel.theaterReportLsit.observe(this,
            Observer {
                var price:Double=0.0
                for(item in reportModel.theaterReportLsit.value!!)
                    price+=item.price.toDouble()
                v!!.theaterDep.text=price.toString()
                updateTotal(price)
            })
    }

    override fun onReportScreenMoneyLoadedFaield() {
        makeToast(getString(R.string.error))
    }

    override fun onReportScreenMoneyLoadedSuccess() {
        reportModel.screenReportLsit.observe(this,
            Observer {
                var price:Double=0.0
                for(item in reportModel.screenReportLsit.value!!)
                    price+=item.price.toDouble()
                v!!.screenDep.text=price.toString()
                updateTotal(price)
            })
    }

}
