package com.karim.myapplication.Activites

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
import java.util.*

class AddWorkDone : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_work_done)
        addWork.setOnClickListener{
            if(employeName.text.equals(""))
                employeName.setError("من فضلك ادخل اسم الموظف")
            else if(workDate.text.equals(""))
                workDate.setError("من فضلك ادخل تاريخ العمل")
            else if(workUri.text.equals(""))
                workUri.setError("من فضلك ادخل رابط العمل")
            else if(workName.text.isEmpty())
                workName.setError("من فضلك أدخل إسم العمل")
            else {
                var work = WorkDone(
                    employeName.text.toString(),
                    workUri.text.toString(),
                    workDate.text.toString(),
                    workName.text.toString()
                )
                val dialog: android.app.AlertDialog? = SpotsDialog.Builder()
                    .setContext(this)
                    .setTheme(R.style.Custom)
                    .build()
                dialog!!.show()
                FirebaseDatabase.getInstance().getReference("WorkDone").child(FirebaseAuth.getInstance().currentUser!!.uid)
                    .child(Calendar.getInstance().time.toString()).setValue(work).addOnCompleteListener(
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
