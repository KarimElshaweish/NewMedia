package com.karim.myapplication.Activites

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.karim.myapplication.model.WorkDone
import com.karim.myapplication.R
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.activity_add_work_done.*
import kotlinx.android.synthetic.main.work_done_table.*
import java.text.SimpleDateFormat
import java.util.*

class AddWorkDone : AppCompatActivity() {
    fun updateDate( date:String){
        workDate.text=date
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_work_done)
        var myCalendar=Calendar.getInstance()
        val dateSetup=DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            myCalendar.set(Calendar.YEAR,year)
            myCalendar.set(Calendar.MONTH,month)
            myCalendar.set(Calendar.DAY_OF_WEEK,dayOfMonth)
            val date =  Date(year, month, dayOfMonth-1)
            val locale=Locale("ar")
            Locale.setDefault(locale)
            val config = getResources().getConfiguration()
            config.setLocale(locale)
            createConfigurationContext(config)
            val  sdf = SimpleDateFormat("EEEE",locale)
            val d=sdf.format(date)
            updateDate("$d-${dayOfMonth}/${month+1}/${year}")
        }

        workDate.setOnClickListener{
            DatePickerDialog(this,R.style.DialogTheme
                ,dateSetup,myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_WEEK)).show()

        }
        val name=intent.getStringExtra("name")
        employeName.text=name
        addWork.setOnClickListener{
            when {
                employeName.text.isEmpty() -> employeName.error = "من فضلك ادخل اسم الموظف"
                workDate.text.isEmpty() -> workDate.error = "من فضلك ادخل تاريخ العمل"
                workUri.text.isEmpty() -> workUri.error = "من فضلك ادخل رابط العمل"
                workName.text.isEmpty() -> workName.error = "من فضلك أدخل إسم العمل"
                else -> {

                    val work = WorkDone(Calendar.getInstance().time.toString(),
                        name,
                        workUri.text.toString(),
                        workDate.text.toString(),
                        workName.text.toString()
                    ,false)
                    val dialog: android.app.AlertDialog? = SpotsDialog.Builder()
                        .setContext(this)
                        .setTheme(R.style.Custom)
                        .build()
                    dialog!!.show()
                    FirebaseDatabase.getInstance().getReference("WorkDone").child(name)
                        .child(work.id).setValue(work).addOnCompleteListener(
                            OnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    dialog.dismiss()
                                    Toast.makeText(baseContext,"تم الإضافة",Toast.LENGTH_SHORT).show()
                                    finish()
                                }else{
                                    dialog.dismiss()
                                    Toast.makeText(baseContext,task.exception!!.message.toString(),Toast.LENGTH_SHORT).show()

                                }
                            })
                }
            }
        }
    }
}
