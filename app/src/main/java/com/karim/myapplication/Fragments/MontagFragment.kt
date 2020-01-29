package com.karim.myapplication.Fragments

import android.annotation.SuppressLint
import android.app.DatePickerDialog
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
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.karim.myapplication.Notification.*
import com.karim.myapplication.model.Montag
import com.karim.myapplication.R
import kotlinx.android.synthetic.main.fragment_montag.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*


class MontagFragment : Fragment() {


    lateinit var nameText:String
    lateinit var phoneNumber:String
    lateinit var dateText:String
    lateinit var cashGet:String
    lateinit var cashRest:String
    fun changeNumbers(number:String):String{
        var arabNumbers= arrayListOf<Char>('٠','١','٢','٣','٤','٥','٦','٧','٨','٩')
       for (i in '0' .. '9'){
           if(number.contains(i)){
               val index=i.toString().toInt()
               number.replace(i,arabNumbers[index])
           }
       }
        return number
    }
    fun getArabicNumber(number:Int)=String.format("%d", number)
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
    lateinit var calendar: Calendar
    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        builder.detectFileUriExposure()
        // Inflate the layout for this fragment
        var view= inflater.inflate(R.layout.fragment_montag, container, false)
        calendar=Calendar.getInstance()
        var locale=Locale("ar")
        Locale.setDefault(locale)
        var config =
            context!!.getResources().getConfiguration()
        config.setLocale(locale)
        var workDate=view.findViewById<TextView>(R.id.workDate)
        var dateSetup=DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR,year)
            calendar.set(Calendar.MONTH,month)
            calendar.set(Calendar.DAY_OF_WEEK,dayOfMonth)
            var date =  Date(year, month, dayOfMonth-1)
            context!!.createConfigurationContext(config)
            var  sdf = SimpleDateFormat("EEEE",locale)
            var d=sdf.format(date)
            workDate.text="$d-${getArabicNumber(dayOfMonth)}/${getArabicNumber(month+1)}/${getArabicNumber(year)}"
        }
        val checkbox=view.findViewById<CheckBox>(R.id.allMoneyCheckBox)
        checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                view.resstPriceText.isEnabled=false
                view.resstPriceText.setText("0")
            }else{
                view.resstPriceText.isEnabled=true
            }
        }
        view.workDate.setOnClickListener{
            DatePickerDialog(context!!,R.style.DialogTheme
                ,dateSetup
                ,calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_WEEK)).show()


        }
        activity?.let {
            ActivityCompat.requestPermissions(
                it,
              arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                  android.Manifest.permission.READ_EXTERNAL_STORAGE),
                1)
        }
        scrollView=view.findViewById(R.id.scroll_print)
        report=view.findViewById(R.id.fullReportOption)
        var btnDeal=view.findViewById<Button>(R.id.addContract)
        btnDeal.setOnClickListener{
            nameText = view.clientName.text.toString()
            phoneNumber = view.phonNumber.text.toString()
            dateText = view.workDate.text.toString()
            cashGet = view.priceText.text.toString()
            cashRest = view.resstPriceText.text.toString()
            if(nameText.isNotEmpty()&&dateText.isNotEmpty()&&
                    phoneNumber.isNotEmpty()&&cashGet.isNotEmpty()&&
                    cashRest.isNotEmpty()) {
                scrollView.visibility = View.GONE
                report.visibility = View.VISIBLE
                view.report_data.text = dateText
                view.clientNameReport.text = nameText
                view.clientPhoneNumberReport.text = phoneNumber
                view.cashGet_report.text = cashGet
                view.cashRest_report.text = cashRest
            }else{
                Toast.makeText(context,getString(R.string.please_enter_all_data),Toast.LENGTH_SHORT).show()
            }
        }
        var print_pdf=view.findViewById<Button>(R.id.print_pdf)
        print_pdf.setOnClickListener{
            val time=Calendar.getInstance().time.toString()
            val uid=FirebaseAuth.getInstance().currentUser!!.uid
            var montag=Montag(nameText,phoneNumber,dateText,cashGet,cashRest,"${uid}*${time}",time,false)
            FirebaseDatabase.getInstance().getReference("Montage").child(uid)
                .child(time)
                .setValue(montag).addOnCompleteListener{ task ->
                    if (task.isSuccessful) {
                        api_services=
                            Client.getClient("https://fcm.googleapis.com/").create(APIServices::class.java)
                        sentNotification("tNhsqknIyefQ1CztCTgiX4XIlCf1",montag.name,montag.date)

                        print_pdf.visibility = View.GONE
                        var printView = view.findViewById<ScrollView>(R.id.report)
                        createPdf(
                            getBitmapFromView(
                                printView,
                                view.report.getChildAt(0).getHeight(),
                                view.report.getWidth()
                            )
                        )
                    } else {
                        Toast.makeText(
                            context,
                            task.exception!!.message.toString(),
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }
        }
        return view
    }
    var api_services: APIServices?=null
    private fun sentNotification(reciver: String, clientName: String, workDate: String) {
        val tokens = FirebaseDatabase.getInstance().getReference("Tokens")

        val query = tokens.orderByKey().equalTo(reciver)
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dt: DataSnapshot) {
                for(dt0 in dt.children) {
                    if (dt0.key == "tNhsqknIyefQ1CztCTgiX4XIlCf1") {
                        val token = dt0.value as HashMap<*, *>
                        var data = Data(
                            FirebaseAuth.getInstance().currentUser!!.uid,
                            clientName, workDate
                        )
                        val sender = Sender(data, token["token"].toString())
                        api_services!!.sendNotificaiton(sender)
                            .enqueue(object : Callback<MyResponse> {
                                override fun onResponse(
                                    call: Call<MyResponse>,
                                    response: Response<MyResponse>
                                ) {
                                    if (response.code() == 200) {
                                    }
                                }

                                override fun onFailure(
                                    call: Call<MyResponse>,
                                    t: Throwable
                                ) {
                                    println(t.message)
                                }
                            })
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

    }

    lateinit var scrollView:ScrollView
    lateinit var report:LinearLayout
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

        val filePath = File(context?.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS),"مونتاج.pdf")
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

}
