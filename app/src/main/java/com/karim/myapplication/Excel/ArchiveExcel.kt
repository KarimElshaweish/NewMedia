package com.karim.myapplication.Excel

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.os.StrictMode
import android.widget.Toast
import com.karim.myapplication.Converter.NumberConverter.Companion.arabNumber
import com.karim.myapplication.model.OrderData
import org.apache.poi.hssf.usermodel.HSSFCellStyle
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.hssf.util.HSSFColor
import org.apache.poi.ss.usermodel.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


@Suppress("NAME_SHADOWING")
class ArchiveExcel(val ordersLsit:MutableList<OrderData>, val _ctx:Context,val name:String){
    private var coloumns= arrayOf("التاريخ","إسم العميل", "المكان","العناصر", "المتبقى","رقم التليفون")
    fun createExcel(){
        coloumns.reverse()
        var builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())

        val wb: Workbook = HSSFWorkbook()
        val cellStyle: CellStyle = wb.createCellStyle()
        cellStyle.setFillForegroundColor(HSSFColor.LIGHT_BLUE.index)
        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND)
        var sheet: Sheet? = null
        sheet = wb.createSheet(name)
        val headerFont= wb.createFont()
        headerFont.boldweight=14
        headerFont.color = IndexedColors.RED.getIndex()
        val headerCellStyle: CellStyle = wb.createCellStyle()
        headerCellStyle.setFont(headerFont)
        headerCellStyle.alignment=CellStyle.ALIGN_CENTER
        headerCellStyle.borderBottom=2
        headerCellStyle.borderLeft=2
        headerCellStyle.borderRight=2
        headerCellStyle.borderTop=2
        val headerRow=sheet.createRow(0)
        for(i in coloumns.indices) {
            val cell = headerRow.createCell(i)
            cell.setCellValue(coloumns[i])
            cell.cellStyle = headerCellStyle
        }
        var csStyle=wb.createCellStyle()
        csStyle.alignment=CellStyle.ALIGN_CENTER
        var intRow=1
                for(order in ordersLsit){
            val row = sheet.createRow(intRow++)
                    val c5= row.createCell(5)
                    c5.cellStyle=csStyle
                    c5.setCellValue(arabNumber(order.date))
                    val c4=row.createCell(4)
                    c4.cellStyle=csStyle
                    c4.setCellValue(order.clientName)
                    val c3=row.createCell(3)
                    c3.cellStyle=csStyle
                    c3.setCellValue(order.location)
                    val c2=row.createCell(2)
                    c2.cellStyle=csStyle
                    c2.setCellValue(order.items)
          //  row.createCell(4).setCellValue(order.moneyGet)
         //   row.createCell(3).setCellValue(order.moneyHave)
            val c1=row.createCell(1)
                    c1.cellStyle=csStyle
                    c1.setCellValue(order.moneyRest)
            val c0=row.createCell(0)
                c0.cellStyle=csStyle
                    c0.setCellValue(order.phoneNumber)
          //  row.createCell(0).setCellValue(order.workName)
        }
        sheet.setColumnWidth(0,3000)
        sheet.setColumnWidth(1, 3000)
        sheet.setColumnWidth(2, 6000)
        sheet.setColumnWidth(3,4000)
        sheet.setColumnWidth(4, 3000)
        sheet.setColumnWidth(5, 3000)
        val file = File(_ctx.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString(), "${name}.xls")
        var outputStream: FileOutputStream? = null

        try {
            outputStream = FileOutputStream(file)
            wb.write(outputStream)
            Toast.makeText(_ctx.applicationContext, "تم إصدار المستند", Toast.LENGTH_LONG).show()
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(_ctx.applicationContext, "حدث خطأ", Toast.LENGTH_LONG).show()
            try {
                outputStream!!.close()
            } catch (ex: IOException) {
                ex.printStackTrace()
            }
        }
        val uri = Uri.fromFile(file)
        val share = Intent()
        share.action = Intent.ACTION_SEND_MULTIPLE
        share.putParcelableArrayListExtra(Intent.EXTRA_STREAM, arrayListOf(uri))
        share.type = "text/plain"
        _ctx.startActivity(share)
    }

}