package com.karim.myapplication.Activites

import android.content.Intent
import android.graphics.*
import android.graphics.pdf.PdfDocument
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.view.Gravity
import android.view.View
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.karim.myapplication.Adapter.RVAdapterPkItems
import com.karim.myapplication.R
import com.karim.myapplication.Repositry.MonthReop
import com.karim.myapplication.Repositry.OrderRepo
import com.karim.myapplication.Repositry.ReportRepo
import com.karim.myapplication.Util
import com.karim.myapplication.model.*
import kotlinx.android.synthetic.main.activity_view_contract.*
import kotlinx.android.synthetic.main.activity_view_contract.report
import kotlinx.android.synthetic.main.activity_view_contract.rvitems
import kotlinx.android.synthetic.main.sound_report.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

class ViewContractActivity : AppCompatActivity() {

    private fun getTheaterLsit(items:ArrayList<String>):MutableList<TheaterData>{
        var list= mutableListOf<TheaterData>()
        for(i in 0 until items.size step 1){
            var map2=items[i]as HashMap<String,String>
            var meter= map2["price"].toString()
            var price= map2["totalMeter"].toString()
           createTableRow(TheaterData(price,meter))
        }
        return list
    }
    private fun createTableRow(obj: Any) {
        var tableRow = TableRow(this)
        if (obj is TheaterData) {
            var theater = obj
            tableRow.addView(createTextLabel(theater.price))
            tableRow.addView(createTextLabel(theater.totalMeter))
            theater_table.addView(tableRow)
        } else {
            var screen=obj as ScreenType
            tableRow.addView(createTextLabel(screen.price))
            tableRow.addView(createTextLabel(screen.totalMeter))
            screen_table.addView(tableRow)
        }
    }
    private fun createTextLabel(text:String): TextView {
        val tableRowparam = TableRow.LayoutParams()
        tableRowparam.weight=1f
        val textView = TextView(this)
        textView.width=resources.getDimension(R.dimen.textViewWidth).toInt()
        textView.setBackgroundDrawable(resources.getDrawable(R.drawable.cell_border))
        textView.layoutParams=tableRowparam
        textView.text = text
        textView.gravity= Gravity.CENTER
        textView.setTextColor(Color.BLACK)
        textView.setPadding(2,2,2,2)
        return textView
    }
    private fun getScreenLsit(items:ArrayList<String>):MutableList<ScreenType>{
        val list= mutableListOf<ScreenType>()
        for(i in 0 until items.size step 1){
            val map2=items[i]as HashMap<String,String>
            val meter= map2["price"].toString()
            val price= map2["totalMeter"].toString()
            createTableRow(ScreenType(price,meter))
        }
        return list
    }
    private fun getList(items: java.util.ArrayList<String>):List<PhotoGraph>{
        var list= mutableListOf<PhotoGraph>()
        for(i in 0 until  items.size step 1){
            var map2=items[i] as HashMap<String, Objects>
            var typelist=getItemsList(map2.get("items") as java.util.ArrayList<String>)
            var phot=
                PhotoGraph(map2.get("name").toString(),map2.get("price").toString(), typelist,map2.get("iamge").toString())
            list.add(phot)
        }

        return list
    }
    private fun getItemsList(items: java.util.ArrayList<String>):List<TypesItems>{
        var list= mutableListOf<TypesItems>()
        for(i in 0 until items.size step 1){
            var map=items[i]as HashMap<String,String>
            var type= TypesItems(map.get("item").toString())
            list.add(type)
        }
        return list
    }
    fun setTheater(orderId:String){
        val split=orderId.split("*").toTypedArray()
        val query=ref.child("TheaterMonth/${split[0]}/${split[1]}")
        query.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
            }
            override fun onDataChange(p0: DataSnapshot) {
                if(p0.value!=null) {
                    val map = p0.value as Map<*, *>
                    val ob = map["theaterList"] as java.util.ArrayList<String>
                    val list = getTheaterLsit(ob)
                    val work = TheaterUploadData(
                        list,
                        map["clientName"].toString(),
                        map["moneyGet"].toString(),
                        map["phoneNumber"].toString(),
                        map["location"].toString(),
                        map["date"].toString(),
                        map["moneyRest"].toString(),
                        map["moneyHave"].toString(),
                        map["id"].toString(),
                        map["workName"].toString(),
                        map["time"].toString()
                    , (map["checked"] as Boolean))
                    theaterClientName.text = work.clientName
                    theaterPhonerNumber.text = work.phoneNumber
                    theaterLocation.text = work.location
                    theaterMoneyGetWay.text = work.moneyGet
                    theaterDate.text = work.date
                    theaterTotalPrice.text =
                        (work.moneyHave.toDouble() + work.moneyRest.toDouble()).toInt().toString()
                    theaterMoneyHave.text = work.moneyHave
                    theaterMoneyRest.text = work.moneyRest
                    theaterReportView.visibility = View.VISIBLE
                }
                else{
                    makeToast(getString(R.string.removed_in_order))
                }
            }
        })
    }
    private fun makeToast(string: String) {
        Toast.makeText(baseContext,string,Toast.LENGTH_SHORT).show()
    }
    fun loadScreen(orderId:String){
        val split=orderId.split("*").toTypedArray()
        val query=ref.child("ScreenMonth/${split[0]}/${split[1]}")
        query.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                OrderRepo.listener?.onScreenloadedFailed()
            }
            override fun onDataChange(p0: DataSnapshot) {
                        val map = p0.value as Map<String, Objects>
                        val ob=map.get("theaterList") as java.util.ArrayList<String>
                        val list=getScreenLsit(ob)
                        var work = ScreenUploadData(
                            list ,
                            map["clientName"].toString(),
                            map["moneyGet"].toString(),
                            map["phoneNumber"].toString(), map["location"].toString(),
                            map["date"].toString(),
                            map["moneyRest"].toString(),
                            map["moneyHave"].toString(),
                            map["id"].toString()
                            , map["workName"].toString(),
                            map["time"].toString(), (map["checked"] as Boolean))
                screenClientName.text=work.clientName
                screenPhoneNumber.text=work.phoneNumber
                screenLocation.text=work.location
                screenMoneyGetWay.text=work.moneyGet
                screenDate.text=work.date
                screenTotalPrice.text=(work.moneyHave.toDouble()+work.moneyRest.toDouble()).toInt().toString()
                screenMoneyHave.text=work.moneyHave
                screenMoneyRest.text=work.moneyRest
                screenReportView.visibility=View.VISIBLE
            }

        })
    }
    private fun loadPhoto(orderId:String) {
        val split=orderId.split("*").toTypedArray()
        val query=ref.child("photoMonth/${split[0]}/${split[1]}")
        query.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
            }
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.value != null) {
                    val map = p0.value as Map<String, Objects>
                    val ob = map.get("items") as java.util.ArrayList<String>
                    val list = getList(ob)
                    val work = photoData(
                        list,
                        map["clientName"].toString(),
                        map["moneyGet"].toString(),
                        map["phoneNumber"].toString(), map["location"].toString(),
                        map["date"].toString(),
                        map["moneyRest"].toString(),
                        map["moneyHave"].toString(),
                        map["id"].toString()
                        , map["workName"].toString(),
                        map["time"].toString()
                    , (map["checked"] as Boolean))
                    photoClientName.text = work.clientName
                    photoPhoneNumber.text = work.phoneNumber
                    photoLocation.text = work.location
                    photoMoneyGetWay.text = work.moneyGet
                    phototDate.text = work.date
                    photoTotalPrice.text =
                        (work.moneyHave.toDouble() + work.moneyRest.toDouble()).toInt().toString()
                    photoMoneyHave.text = work.moneyHave
                    photoMoneyRest.text = work.moneyRest

                    val listTypes1 = mutableListOf<TypesItems>()
                    for (item in list) {
                        listTypes1.addAll(item.items)
                    }
                    rvitems1.setHasFixedSize(true)
                    rvitems1.layoutManager = LinearLayoutManager(baseContext)
                    val itemAdapter1 = RVAdapterPkItems(listTypes1, baseContext, false)
                    rvitems1.adapter = itemAdapter1
                    photoReportView.visibility = View.VISIBLE
                } else {
                    makeToast(getString(R.string.removed_in_order))
                }
            }
        })
    }
    private fun loadMusic(orderId:String) {
        val split=orderId.split("*").toTypedArray()
        val query=ref.child("musicOrdersMonth/${split[0]}/${split[1]}")
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.value != null) {
                    val map = p0.value as Map<*, *>
                    val ob = map["items"] as java.util.ArrayList<String>
                    val list = getList(ob)
                    val work = photoData(
                        list,
                        map["clientName"].toString(),
                        map["moneyGet"].toString(),
                        map["phoneNumber"].toString(), map["location"].toString(),
                        map["date"].toString(),
                        map["moneyRest"].toString(),
                        map["moneyHave"].toString(),
                        map["id"].toString()
                        , map["workName"].toString(),
                        map["time"].toString()
                    , (map["checked"] as Boolean))

                    soundClientName.text = work.clientName
                    soundPhoneNumber.text = work.phoneNumber
                    soundLocation.text = work.location
                    soundMoneyGetWay.text = work.moneyGet
                    soundtDate.text = work.date
                    soundTotalPrice.text =
                        (work.moneyHave.toDouble() + work.moneyRest.toDouble()).toInt().toString()
                    soundMoneyHave.text = work.moneyHave
                    soundMoneyRest.text = work.moneyRest

                    soundReportView.visibility = View.VISIBLE


                    rvitems.setHasFixedSize(true)
                    rvitems.layoutManager = LinearLayoutManager(baseContext)
                    val listTypes = mutableListOf<TypesItems>()
                    for (item in list) {
                        listTypes.addAll(item.items)
                    }
                    val itemAdapter = RVAdapterPkItems(listTypes, baseContext, false)
                    rvitems.adapter = itemAdapter
                } else {
                    makeToast(getString(R.string.removed_in_order))
                }
            }
        })
    }
    private fun loadMontag(orderId:String){
        val split=orderId.split("*").toTypedArray()
        val query=ref.child("Montage/${split[0]}/${split[1]}")
        query.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                MonthReop.listener!!.onMontageFaield()
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.value != null) {
                    val map = p0.value as Map<*, *>
                    val cashGet = map["cashGet"].toString()
                    val cashRest = map["cashRest"].toString()
                    val date = map["date"].toString()
                    val id = map["id"].toString()
                    val name = map["name"].toString()
                    val phoneNumber = map["phoneNumber"].toString()
                    val montag = Montag(name, phoneNumber, date, cashGet, cashRest, id,map["time"].toString(), (map["checked"] as Boolean))
                    montagClientName.text = montag.name
                    montagPhoneNumber.text = montag.phoneNumber
                    montagDate.text = montag.date
                    montagMoneyHave.text = montag.cashGet
                    montagMoneyRest.text = montag.cashRest
                    montageReport.visibility = View.VISIBLE
                } else {
                    makeToast(getString(R.string.removed_in_order))
                }
            }
        })
    }
    val ref=FirebaseDatabase.getInstance().reference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_contract)
        val id= intent.extras?.get("id")
        val orderId=intent.extras?.get("orderID").toString()
        when(id.toString().toInt()){
            1->{
                //theater
                setTheater(orderId)
                shareBtn.setOnClickListener{
                    createPdf(
                        getBitmapFromView(
                            theaterReportView,
                            theaterReportView.getChildAt(0).height,
                            theaterReportView.width
                        )
                    )
                }
            }
            2->{
                //screen
                loadScreen(orderId)
                shareBtn.setOnClickListener{
                    createPdf(
                        getBitmapFromView(
                            screenReportView,
                            screenReportView.getChildAt(0).height,
                            screenReportView.width
                        )
                    )
                }
            }
            3->{
                //photo
                loadPhoto(orderId)
                shareBtn.setOnClickListener{
                    createPdf(
                        getBitmapFromView(
                            photoReportView,
                            photoReportView.getChildAt(0).height,
                            photoReportView.width
                        )
                    )
                }
            }
            4->{
                //sound
                loadMusic(orderId)
                shareBtn.setOnClickListener{
                    createPdf(
                        getBitmapFromView(
                            soundReportView,
                            soundReportView.getChildAt(0).height,
                            soundReportView.width
                        )
                    )
                }
            }
            5->{
                //montage
                loadMontag(orderId)
                shareBtn.setOnClickListener{
                    createPdf(
                        getBitmapFromView(
                            montageReport,
                            montageReport.getChildAt(0).height,
                            montageReport.width
                        )
                    )
                }
            }
        }
    }
    var document:PdfDocument?=null
    private fun createPdf(bitmap: Bitmap) {

        var builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        document = PdfDocument()
            var bitmap = bitmap
            val pageInfo = PdfDocument.PageInfo.Builder(bitmap.width, bitmap.height, 1).create()
            val page = document!!.startPage(pageInfo)
            val canvas = page.getCanvas()

            val paint = Paint()
            paint.color = Color.parseColor("#ffffff")
            canvas.drawPaint(paint)


            bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.width, bitmap.height, true)

            paint.color = Color.BLUE
            canvas.drawBitmap(bitmap, 0f, 0f, null)
            document!!.finishPage(page)

        // write the document content
        // val targetPdf = context?.getExternalFilesDir(null).toString()+"/hello.pdf"
        //val targetPdf =  Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)c

        share()
        // close the document
        }
    private fun getBitmapFromView(view: View, height: Int, width: Int): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        //
        val ratioX = width / bitmap.width.toFloat()
        val ratioY = height / bitmap.height.toFloat()
        val middleX = width / 2.0f
        val middleY = height / 2.0f

        ///
        val scaleMatrix = Matrix()
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY)


        val canvas = Canvas(bitmap)
        ///
        canvas.setMatrix(scaleMatrix)
        canvas.drawBitmap(
            bitmap,
            middleX - bitmap.width / 2,
            middleY - bitmap.height / 2,
            Paint(Paint.FILTER_BITMAP_FLAG)
        )
        val bgDrawable = view.background
        if (bgDrawable != null)
            bgDrawable.draw(canvas)
        else
            canvas.drawColor(Color.WHITE)
        view.draw(canvas)
        return bitmap
    }
    var filePath:File?=null
    private fun share() {
        filePath =
            File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "عقد نيوميديا.pdf")
        try {
            document!!.writeTo(FileOutputStream(filePath))
        } catch (kesho: IOException) {
            kesho.printStackTrace()
            Toast.makeText(this, "Something wrong: " + kesho.toString(), Toast.LENGTH_LONG)
                .show()
        }
        val uri = Uri.fromFile(filePath)

        val share = Intent()
        share.setAction(Intent.ACTION_SEND_MULTIPLE)
        share.putParcelableArrayListExtra(Intent.EXTRA_STREAM, arrayListOf(uri))
        share.setType("text/plain")
        startActivity(share)

    }
}
