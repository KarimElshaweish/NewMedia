package com.karim.myapplication.Excel

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.os.StrictMode
import android.widget.Toast
import com.karim.myapplication.model.OrderData
import org.apache.poi.hssf.usermodel.HSSFCellStyle
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.hssf.util.HSSFColor
import org.apache.poi.ss.usermodel.CellStyle
import org.apache.poi.ss.usermodel.IndexedColors
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


@Suppress("NAME_SHADOWING")
class ArchiveExcel(val ordersLsit:MutableList<OrderData>, val _ctx:Context,val name:String){
    private var coloumns= arrayOf("إسم العميل","التاريخ","العناصر",
    "المكان","طريقةالدفع", 	"العربون",	"المتبقى",	"رقم التليفون","إسم العمل")
    fun createExcel(){
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
        var intRow=1
                for(order in ordersLsit){
            val row = sheet.createRow(intRow++)
            row.createCell(0).setCellValue(order.clientName)
            row.createCell(1).setCellValue(order.date)
            row.createCell(2).setCellValue(order.items)
            row.createCell(3).setCellValue(order.location)
            row.createCell(4).setCellValue(order.moneyGet)
            row.createCell(5).setCellValue(order.moneyHave)
            row.createCell(6).setCellValue(order.moneyRest)
            row.createCell(7).setCellValue(order.phoneNumber)
            row.createCell(8).setCellValue(order.workName)
        }
        sheet.setColumnWidth(0, 10 * 200)
        sheet.setColumnWidth(1, 10 * 200)
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