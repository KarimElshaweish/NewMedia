package com.karim.myapplication.Print

import android.content.Context
import android.content.Intent
import android.graphics.*
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Environment
import android.os.StrictMode
import android.util.LruCache
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class PrintPDF(val _ctx:Context) {
    private var document:PdfDocument?=null
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
    fun createPdf(view: View, height: Int, width: Int) {
       var bitmap=getBitmapFromView(view,height,width)
        var builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        document = PdfDocument()
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
        share()
    }
    var filePath:File?=null
    private fun share() {
        filePath =
            File(_ctx.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "حجوزات.pdf")
        try {
            document!!.writeTo(FileOutputStream(filePath))
        } catch (kesho: IOException) {
            kesho.printStackTrace()
            Toast.makeText(_ctx, "Something wrong: " + kesho.toString(), Toast.LENGTH_LONG)
                .show()
        }
        val uri = Uri.fromFile(filePath)

        val share = Intent()
        share.setAction(Intent.ACTION_SEND_MULTIPLE)
        share.putParcelableArrayListExtra(Intent.EXTRA_STREAM, arrayListOf(uri))
        share.setType("text/plain")
        _ctx.startActivity(share)
    }

    fun getScreenshotFromRecyclerView(view: RecyclerView): Bitmap? {
        val adapter = view.adapter
        var bigBitmap: Bitmap? = null
        if (adapter != null) {
            val size = adapter.itemCount
            var height = 0
            val paint = Paint()
            var iHeight = 0
            val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()
            // Use 1/8th of the available memory for this memory cache.
            val cacheSize = maxMemory / 8
            val bitmaCache: LruCache<String, Bitmap> = LruCache(cacheSize)
            for (i in 0 until size) {
                val holder =
                    adapter.createViewHolder(view, adapter.getItemViewType(i))
                adapter.onBindViewHolder(holder, i)
                holder.itemView.measure(
                    View.MeasureSpec.makeMeasureSpec(
                        view.width,
                        View.MeasureSpec.EXACTLY
                    ),
                    View.MeasureSpec.makeMeasureSpec(
                        0,
                        View.MeasureSpec.UNSPECIFIED
                    )
                )
                holder.itemView.layout(
                    0,
                    0,
                    holder.itemView.measuredWidth,
                    holder.itemView.measuredHeight
                )
                holder.itemView.isDrawingCacheEnabled = true
                holder.itemView.buildDrawingCache()
                val drawingCache = holder.itemView.drawingCache
                if (drawingCache != null) {
                    bitmaCache.put(i.toString(), drawingCache)
                }
                height += holder.itemView.measuredHeight
            }
            bigBitmap =
                Bitmap.createBitmap(view.measuredWidth, height, Bitmap.Config.ARGB_8888)
            val bigCanvas = Canvas(bigBitmap)
            bigCanvas.drawColor(Color.WHITE)
            for (i in 0 until size) {
                val bitmap: Bitmap = bitmaCache.get(i.toString())
                bigCanvas.drawBitmap(bitmap, 0f, iHeight.toFloat(), paint)
                iHeight += bitmap.height
                bitmap.recycle()
            }
        }
        return bigBitmap
    }

}