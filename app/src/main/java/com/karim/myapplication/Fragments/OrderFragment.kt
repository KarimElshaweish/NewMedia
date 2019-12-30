package com.karim.myapplication.Fragments

import android.app.ActionBar
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.Rect
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.karim.myapplication.Activites.Orders.MontageOrderAct
import com.karim.myapplication.Activites.Orders.PhotoOrderActivity
import com.karim.myapplication.OnOrdersDataLoaderLisenter
import com.karim.myapplication.R
import com.karim.myapplication.ViewModel.OrdersViewModel
import com.karim.myapplication.model.*
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.activity_montage_order.*
import kotlinx.android.synthetic.main.activity_montage_order.table
import kotlinx.android.synthetic.main.fragment_order.*
import kotlinx.android.synthetic.main.fragment_order.view.*
import java.util.*

class OrderFragment : Fragment(),OnOrdersDataLoaderLisenter {


    var orderModel=OrdersViewModel()
    var dialog: AlertDialog? =null
    var v: View? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_order, container, false)
        dialog= SpotsDialog.Builder()
            .setContext(context!!)
            .setTheme(R.style.upload)
            .build()
        dialog!!.show()
        orderModel=ViewModelProviders.of(this).get(OrdersViewModel::class.java)
        orderModel.init(this)
        orderModel.getPhoto()
        return v
    }
    var max=0
    private fun theaterTableRow(work:TheaterUploadData,view:View){
        var itemStr=""
        var price=""
        for(item in work.theaterList){
                itemStr +=        " مسرح "+    item.totalMeter+" متر "
            price=item.price
        }
        var tableRow= TableRow(context!!)
        tableRow.setLayoutParams(
            ActionBar.LayoutParams(
                ViewGroup.LayoutParams.FILL_PARENT
            ))
            tableRow.addView(createTextLabel(work.date))
            tableRow.addView(createTextLabel(work.phoneNumber))
            tableRow.addView(createTextLabel(work.location))
            tableRow.addView(createTextLabel(work.moneyGet))
            tableRow.addView(createTextLabel(itemStr))
            tableRow.addView(createTextLabel(work.moneyRest))
            tableRow.addView(createTextLabel(work.moneyHave))
            tableRow.addView(createTextLabel(price))
            tableRow.addView(createTextLabel(work.clientName))
            view.table.addView(tableRow)
    }
    private fun screenTableRow(work:ScreenUploadData,view:View){
        var itemStr=""
        var price=""
        for(item in work.theaterList){
                itemStr +=        " شاشة "+    item.totalMeter+" متر "
            price=item.price
        }
        var tableRow= TableRow(context!!)
        tableRow.setLayoutParams(
            ActionBar.LayoutParams(
                ViewGroup.LayoutParams.FILL_PARENT
            ))
            tableRow.addView(createTextLabel(work.date))
            tableRow.addView(createTextLabel(work.phoneNumber))
            tableRow.addView(createTextLabel(work.location))
            tableRow.addView(createTextLabel(work.moneyGet))
            tableRow.addView(createTextLabel(itemStr))
            tableRow.addView(createTextLabel(work.moneyRest))
            tableRow.addView(createTextLabel(work.moneyHave))
            tableRow.addView(createTextLabel(price))
            tableRow.addView(createTextLabel(work.clientName))
            view.table.addView(tableRow)
    }
    private fun createTableRow(work: photoData,view:View) {

        var itemStr=""
        for(item in work.items){
            for(i in item.items) {
                itemStr +=i.item+"\n"
            }
            max= heightcheck(itemStr)
        }
        itemStr=""
        for(item in work.items){
            var tableRow= TableRow(context!!)
            tableRow.setLayoutParams(
                ActionBar.LayoutParams(
                    ViewGroup.LayoutParams.FILL_PARENT
                )
            )
            tableRow.addView(createTextLabel(work.date))
            tableRow.addView(createTextLabel(work.phoneNumber))
            tableRow.addView(createTextLabel(work.location))
            tableRow.addView(createTextLabel(work.moneyGet))
            for(i in item.items) {
                itemStr +=i.item+" -"
            }
            tableRow.addView(createTextLabel(itemStr))
            tableRow.addView(createTextLabel(work.moneyRest))
            tableRow.addView(createTextLabel(work.moneyHave))
            tableRow.addView(createTextLabel(item.price))
            tableRow.addView(createTextLabel(work.clientName))
            view.table.addView(tableRow)
        }


    }

    private fun heightcheck(text: String):Int {
        val tableRowparam = TableRow.LayoutParams()
        tableRowparam.weight=1f
        val textView = TextView(context!!)
        textView.setBackgroundDrawable(resources.getDrawable(R.drawable.cell_border))
        textView.layoutParams=tableRowparam
        textView.text = text
        textView.gravity= Gravity.CENTER
        textView.setTextColor(Color.BLACK)
        textView.setPadding(5, 5, 5, 5)


        val bounds = Rect()
        val textPaint = textView.paint
        textPaint.getTextBounds(text, 0, textView.length(), bounds)
        val height = bounds.height()
        val width = bounds.width()
        return height
    }

    private fun createTextLabel(text:String): TextView {
        val tableRowparam = TableRow.LayoutParams()
        val textView = TextView(context)
        textView.height=max*4+10
        textView.width=resources.getDimension(R.dimen.textViewWidth).toInt()
        textView.setBackgroundDrawable(resources.getDrawable(R.drawable.cell_border))
        textView.layoutParams=tableRowparam
        textView.text = text
        textView.gravity= Gravity.CENTER
        textView.setTextColor(Color.BLACK)
        textView.setPadding(2,2,2,2)
        return textView
    }

    override fun onPicutersOrderLoad() {
        orderModel.photList.observe(
            this,Observer<Any>{
                for(work in orderModel.photList.value!!) {
                    createTableRow(work, v!!)
                    dialog!!.dismiss()
                }
            }
        )
    }

    override fun onMusicOrderLoad() {
        orderModel.soundList.observe(
            this, Observer<Any>{
                for(work in orderModel.soundList.value!!){
                    createTableRow(work,v!!)
                    dialog!!.dismiss()
                }
            }
        )
        orderModel.getSound()
    }

    override fun onPictureFailed() {
        orderModel.getSound()
        dialog!!.dismiss()
        Toast.makeText(context,"حدث خطأ",Toast.LENGTH_SHORT).show()
    }

    override fun onMusicFailed() {
        dialog!!.dismiss()
        Toast.makeText(context,"حدث خطأ",Toast.LENGTH_SHORT).show()
    }

    override fun onScreenloadedSuceess() {
        orderModel.screenList.observe(
            this, Observer<Any>{
                for(wor in orderModel.screenList.value!!){
                    screenTableRow(wor,v!!)
                }
            }
        )
    }

    override fun onScreenloadedFailed() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onTheaterloadedSuccess() {
        orderModel.theaterList.observe(
            this, Observer <Any>{
                for(work in orderModel.theaterList.value!!)
                    theaterTableRow(work,v!!)
            }
        )
    }

    override fun onTheaterloadedFailed() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
