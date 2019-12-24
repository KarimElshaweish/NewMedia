package com.karim.myapplication.Activites.Basket

import android.app.ActionBar
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
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.karim.myapplication.Adapter.BasketRVAdapter
import com.karim.myapplication.Adapter.RVAdapterPkItems
import com.karim.myapplication.Model.TypesItems
import com.karim.myapplication.R
import com.karim.myapplication.Util
import kotlinx.android.synthetic.main.activity_photo_and_sound_basket.*
import kotlinx.android.synthetic.main.fragment_basket.*
import kotlinx.android.synthetic.main.fragment_basket.bsketFrame

import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import android.widget.TextView
import android.widget.TableLayout
import android.view.ViewGroup.LayoutParams.*






class TheaterAndScreensBasket : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_theater_and_screens_basket)
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        builder.detectFileUriExposure()
        var printBtn=findViewById<Button>(R.id.print_pdf)
        var rv=findViewById<RecyclerView>(R.id.rv)
        rv.setHasFixedSize(true)
        rv.layoutManager= LinearLayoutManager(this)
        printBtn.setOnClickListener{
            printBtn.visibility= View.GONE
            bsketFrame.visibility= View.GONE
        //    fullReportOption.visibility= View.VISIBLE
        }
      //  var adapter= BasketRVAdapter(this, Util.listScreen)

     //   rv.adapter=adapter
        var table=findViewById<TableLayout>(R.id.table)

        var sum:Double=0.0
        for(item in Util.listScreen) {
            var tr_head=TableRow(this)
            tr_head.setLayoutParams(
                ActionBar.LayoutParams(
                    MATCH_PARENT,
                    WRAP_CONTENT
                )
            )
            tr_head.addView(createTextLabel(item.price+" ريال "))
            sum+=item.price.toDouble()
            tr_head.addView(createTextLabel(item.totalMeter+" متر "))
            tr_head.addView(createTextLabel(item.type))
            table.addView(
                tr_head, TableLayout.LayoutParams(
                    FILL_PARENT, //part4
                    MATCH_PARENT,1f
                )
            )
        }
      //  cashRest_report.setText(sum.toString())

    }
    private fun createTextLabel(text:String):TextView{
        val tableRowparam = TableRow.LayoutParams()
        tableRowparam.weight=1f
        val textView = TextView(this)
        textView.setBackgroundDrawable(resources.getDrawable(R.drawable.cell_border))
        textView.layoutParams=tableRowparam
        textView.text = text
        textView.gravity=Gravity.CENTER
        textView.setTextColor(Color.BLACK)
        textView.setPadding(5, 5, 5, 5)
        return textView
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
