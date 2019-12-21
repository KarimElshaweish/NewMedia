package com.karim.myapplication.Activites.Basket

import android.content.Intent
import android.graphics.*
import android.graphics.pdf.PdfDocument
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Button
import android.widget.CalendarView
import android.widget.ScrollView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.karim.myapplication.Adapter.BasketRVAdapter
import com.karim.myapplication.Adapter.RVAdapterPkItems
import com.karim.myapplication.Model.TypesItems
import com.karim.myapplication.Model.photoData
import com.karim.myapplication.R
import com.karim.myapplication.Util
import kotlinx.android.synthetic.main.activity_photo_and_sound_basket.*
import kotlinx.android.synthetic.main.fragment_basket.*
import kotlinx.android.synthetic.main.fragment_basket.bsketFrame
import kotlinx.android.synthetic.main.fragment_basket.cashRest_report
import kotlinx.android.synthetic.main.fragment_basket.fullReportOption
import kotlinx.android.synthetic.main.fragment_basket.report
import kotlinx.android.synthetic.main.fragment_basket.view.*
import kotlinx.android.synthetic.main.fragment_montag.view.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

class PhotoAndSoundBasket : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_and_sound_basket)
        var printBtn=findViewById<Button>(R.id.print_pdf)
        var rv=findViewById<RecyclerView>(R.id.rv)
        rv.setHasFixedSize(true)
        rv.layoutManager= LinearLayoutManager(this)
        printBtn.setOnClickListener{
            if(clientName.text.toString().isEmpty())
                clientName.error="من فضلك أدخل إسم العميل"
            else if(moneyGetWay.text.toString().isEmpty())
                moneyGetWay.error="من فضلك ادخل طريقة الدفع"
            else {
                reportClientName.text = clientName.text.toString()
                reportCashType.text = moneyGetWay.text.toString()
                phoneNumberReport.text=phoneNumber.text.toString()
                locationReport.text=workLocation.text.toString()
                printBtn.visibility = View.GONE
                bsketFrame.visibility = View.GONE
                fullReportOption.visibility = View.VISIBLE
                var rvItems=findViewById<RecyclerView>(R.id.rvitems)
                rvItems.setHasFixedSize(true)
                rvItems.layoutManager= LinearLayoutManager(this)

                var listTypes= mutableListOf<TypesItems>()
                var totPrice=0
                for(item in Util.list){
                    listTypes.addAll(item.items)
                    totPrice+=item.price.toInt()
                }
                var itemAdapter=  RVAdapterPkItems(listTypes, this)
                rvItems.adapter=itemAdapter

                cashRest_report.text=totPrice.toString()
            }
        }
        var adapter= BasketRVAdapter(this, Util.list)
        rv.adapter=adapter
        var print_doc_pdf=findViewById<Button>(R.id.print_doc_pdf)
        var printView=findViewById<ScrollView>(R.id.report)
        print_doc_pdf.setOnClickListener {
            print_doc_pdf.visibility = View.GONE
            var photoData= photoData(
                Util.list
                ,clientName.text.toString()
                ,moneyGetWay.text.toString(),phoneNumber.text.toString(),workLocation.text.toString())

            FirebaseDatabase.getInstance().getReference("PhotoData").child(FirebaseAuth.getInstance().currentUser!!.uid)
                .child(Calendar.getInstance().time.toString())
                .setValue(photoData).addOnCompleteListener(OnCompleteListener { task ->
                    if(task.isSuccessful){
                        createPdf(
                            getBitmapFromView(
                                printView,
                                report.getChildAt(0).getHeight(),
                                report.getWidth()
                            )
                        )
                    }else{
                        Toast.makeText(baseContext,task.exception!!.message,Toast.LENGTH_SHORT).show()
                    }
                })

        }
        if(Util.list.size==0){
            noPk.visibility= View.VISIBLE
            printBtn.visibility= View.GONE
        }
    }
    private fun createPdf(bitmap: Bitmap) {
        var bitmap = bitmap
        val document = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(bitmap.width, bitmap.height, 1).create()
        val page = document.startPage(pageInfo)

        val canvas = page.getCanvas()


        val paint = Paint()
        paint.color = Color.parseColor("#ffffff")
        canvas.drawPaint(paint)


        bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.width, bitmap.height, true)

        paint.color = Color.BLUE
        canvas.drawBitmap(bitmap, 0f, 0f, null)
        document.finishPage(page)


        // write the document content
        // val targetPdf = context?.getExternalFilesDir(null).toString()+"/hello.pdf"
        //val targetPdf =  Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)c

        val filePath = File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS),"hello1.pdf")
        try {
            document.writeTo(FileOutputStream(filePath))
        } catch (kesho: IOException) {
            kesho.printStackTrace()
            Toast.makeText(this, "Something wrong: " + kesho.toString(), Toast.LENGTH_LONG).show()
        }
        val uri = Uri.fromFile(filePath)

        val share = Intent()
        share.setAction(Intent.ACTION_SEND_MULTIPLE);
        share.putParcelableArrayListExtra(Intent.EXTRA_STREAM, arrayListOf(uri))
        share.setType("text/plain");
        startActivity(share);
        // close the document
        document.close()

    }
    private fun getBitmapFromView(view: View, height: Int, width: Int): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        ///
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

}
