package com.karim.myapplication.Fragments

import android.content.Intent
import android.graphics.*
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.karim.myapplication.Adapter.BasketRVAdapter
import com.karim.myapplication.Adapter.RVAdapterPkItems
import com.karim.myapplication.Model.TypesItems
import com.karim.myapplication.R
import com.karim.myapplication.Util
import kotlinx.android.synthetic.main.fragment_basket.view.*
import kotlinx.android.synthetic.main.sound_report.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class BasketFragment : Fragment() {


    lateinit var printView1:ScrollView
    lateinit var clientName:EditText
    lateinit var moneyGetWay:EditText
    lateinit var workLocation:EditText
    lateinit var phoneNumber:EditText
    lateinit var reportClientName:TextView
    lateinit var phoneNumberReport:TextView
    lateinit var locationReport:TextView
    lateinit var reportCashType:TextView
    lateinit var moneyGet:TextView
    lateinit var cashRest_report:TextView
    lateinit var rvitems:RecyclerView
    lateinit var rvitems1:RecyclerView
    lateinit var reportClientName1:TextView
    lateinit var reportCashType1:TextView
    lateinit var locationReport1:TextView
    lateinit var phoneNumberReport1:TextView
    lateinit var cashRest_report1:TextView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        __init__ref()
        var view= inflater.inflate(R.layout.fragment_basket, container, false)
        cashRest_report1=view.findViewById(R.id.cashRest_report1)
        phoneNumberReport1=view.findViewById(R.id.phoneNumberReport1)
        locationReport1=view.findViewById(R.id.locationReport1)
        reportClientName1=view.findViewById(R.id.reportClientName1)
        reportCashType1=view.findViewById(R.id.reportCashType1)
        rvitems=view.findViewById(R.id.rvitems)
        rvitems1=view.findViewById(R.id.rvitems1)
        cashRest_report=view.findViewById(R.id.cashRest_report)
        reportClientName=view.findViewById(R.id.reportClientName)
        phoneNumberReport=view.findViewById(R.id.phoneNumberReport)
        locationReport=view.findViewById(R.id.locationReport)
        reportCashType=view.findViewById(R.id.reportCashType)
        moneyGet=view.findViewById(R.id.moneyGet)
        clientName=view.findViewById(R.id.clientName)
        moneyGetWay=view.findViewById(R.id.moneyGetWay)
        workLocation=view.findViewById(R.id.workLocation)
        phoneNumber=view.findViewById(R.id.phoneNumber)
        var soundReportView=view.findViewById<View>(R.id.soundReportView)
        var photoReportView=view.findViewById<View>(R.id.photoReportView)
        var bsketFrame=view.findViewById<ScrollView>(R.id.bsketFrame)
        var fullReportOption=view.findViewById<LinearLayout>(R.id.fullReportOption)
        var soundRv=view.findViewById<RecyclerView>(R.id.sound_rv)
        soundRv.setHasFixedSize(true)
        soundRv.layoutManager=LinearLayoutManager(context)
        var photoAdapter= BasketRVAdapter(context!!, Util.list)
        soundRv.adapter=photoAdapter

        var photoRv=view.findViewById<RecyclerView>(R.id.photo_rv)
        photoRv.setHasFixedSize(true)

        photoRv.layoutManager=LinearLayoutManager(context)
        var soundAdapter= BasketRVAdapter(context!!, Util.soundList)
        photoRv.adapter=soundAdapter

        var svPhoto=view.findViewById<ScrollView>(R.id.sv_photo)
        printView1=view.findViewById<ScrollView>(R.id.report)
        view.print_doc_pdf.setOnClickListener{
            createPdf(getBitmapFromView(
                svPhoto,
                svPhoto.getChildAt(0).getHeight(),
                svPhoto.getWidth()
            ))
        }
        var getDoc=view.findViewById<Button>(R.id.print_pdf)
        getDoc.setOnClickListener{
            var clientName=view.findViewById<EditText>(R.id.clientName)
            var moneyGetWay=view.findViewById<EditText>(R.id.moneyGetWay)
            var workLocation=view.findViewById<EditText>(R.id.workLocation)
            var phoneNumber=view.findViewById<EditText>(R.id.phoneNumber)
            if(clientName.text.toString().isEmpty())
                clientName.setError("من فضلك أدخل اسم العميل")
            else if(moneyGetWay.text.toString().isEmpty())
                moneyGetWay.setError("من فضلك أدخل طريقة الدفع")
            else if(workLocation.text.toString().isEmpty())
                workLocation.setError("من فضلك أدخل مكان العمل")
            else if(phoneNumber.text.toString().isEmpty())
                phoneNumber.setError("من فضلك ادخل رقم الجوال")
            else{
                if(Util.list.size==0&&Util.soundList.size==0)
                    Toast.makeText(context,"لا توجد باقات مضافة",Toast.LENGTH_SHORT).show()
                else if (Util.list.size==0) {
                    reportClientName.text=clientName.text.toString()
                    reportCashType.text=moneyGetWay.text.toString()
                    locationReport.text=workLocation.text.toString()
                    phoneNumberReport.text=phoneNumber.text.toString()
                    cashRest_report.text=moneyGet.text.toString()
                    rvitems.setHasFixedSize(true)
                    rvitems.layoutManager=LinearLayoutManager(context)
                    var listTypes= mutableListOf<TypesItems>()
                    var totPrice=0
                    for(item in Util.soundList){
                        listTypes.addAll(item.items)
                        totPrice+=item.price.toInt()
                    }
                    var itemAdapter=  RVAdapterPkItems(listTypes, context!!)
                    rvitems.adapter=itemAdapter
                    bsketFrame.visibility = View.GONE
                    fullReportOption.visibility = View.VISIBLE
                    photoReportView.visibility=View.GONE
                }else if (Util.soundList.size==0) {


                    bsketFrame.visibility = View.GONE
                    fullReportOption.visibility = View.VISIBLE
                    soundReportView.visibility=View.GONE
                }else{
                    reportClientName.text=clientName.text.toString()
                    reportCashType.text=moneyGetWay.text.toString()
                    locationReport.text=workLocation.text.toString()
                    phoneNumberReport.text=phoneNumber.text.toString()
                    cashRest_report.text=moneyGet.text.toString()
                    rvitems.setHasFixedSize(true)
                    rvitems.layoutManager=LinearLayoutManager(context)
                    var listTypes= mutableListOf<TypesItems>()
                    var totPrice=0
                    for(item in Util.soundList){
                        listTypes.addAll(item.items)
                        totPrice+=item.price.toInt()
                    }
                    var itemAdapter=  RVAdapterPkItems(listTypes, context!!)
                    rvitems.adapter=itemAdapter

                    reportClientName1.text=clientName.text.toString()
                    reportCashType1.text=moneyGetWay.text.toString()
                    locationReport1.text=workLocation.text.toString()
                    phoneNumberReport1.text=phoneNumber.text.toString()
                    cashRest_report1.text=moneyGet.text.toString()

                    rvitems1.setHasFixedSize(true)
                    rvitems1.layoutManager=LinearLayoutManager(context)
                    var listTypes1= mutableListOf<TypesItems>()
                    var totPrice1=0
                    for(item in Util.list){
                        listTypes1.addAll(item.items)
                        totPrice1+=item.price.toInt()
                    }
                    var itemAdapter1=  RVAdapterPkItems(listTypes1, context!!)
                    rvitems1.adapter=itemAdapter1

                    bsketFrame.visibility = View.GONE
                    fullReportOption.visibility = View.VISIBLE
                }
            }

        }
        return view
    }

    private fun __init__ref() {
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        builder.detectFileUriExposure()
    }

    private fun createPdf(bitmap: Bitmap) {


        var builder=StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
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

        var bitmap1 = getBitmapFromView(
            printView1,
            report.getChildAt(0).getHeight(),
            report.getWidth()
        )
        val pageInfo1 = PdfDocument.PageInfo.Builder(bitmap1.width, bitmap1.height, 1).create()
        val page1 = document.startPage(pageInfo)

        val canvas1 = page1.getCanvas()


        val paint1 = Paint()
        paint1.color = Color.parseColor("#ffffff")
        canvas1.drawPaint(paint1)


        bitmap1 = Bitmap.createScaledBitmap(bitmap1, bitmap1.width, bitmap1.height, true)

        paint1.color = Color.BLUE
        canvas1.drawBitmap(bitmap1, 0f, 0f, null)
        document.finishPage(page1)


        // write the document content
        // val targetPdf = context?.getExternalFilesDir(null).toString()+"/hello.pdf"
        //val targetPdf =  Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)c

        val filePath = File(context!!.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS),"hello1.pdf")
        try {
            document.writeTo(FileOutputStream(filePath))
        } catch (kesho: IOException) {
            kesho.printStackTrace()
            Toast.makeText(context, "Something wrong: " + kesho.toString(), Toast.LENGTH_LONG).show()
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
