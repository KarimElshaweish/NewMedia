package com.karim.myapplication.Activites

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.karim.myapplication.R
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.activity_add_employee.*
import java.util.*

class AddEmployee : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_employee)
    }

    fun back(view: View) {
        finish()
    }

    fun addEmployee(view: View) {
        if(employeName.text.isEmpty())
            Snackbar.make(view,getString(R.string.please_enter_all_data),Snackbar.LENGTH_SHORT).show()
        else{
            uploadEmploye()
        }
    }

    private fun makeToast(msg:String){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show()
    }
    private fun uploadEmploye() {
        val dialog: android.app.AlertDialog? = SpotsDialog.Builder()
            .setContext(this)
            .setTheme(R.style.addEmployee)
            .build()
        dialog!!.show()
        val ref=FirebaseDatabase.getInstance().reference
        ref.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                dialog.dismiss()
                makeToast(getString(R.string.error))
            }

            override fun onDataChange(p0: DataSnapshot) {
                val query=ref.child("employees")
                if(!p0.hasChild("employees")){
                    query.child(Calendar.getInstance().time.toString()).setValue(employeName.text.toString())
                        .addOnCompleteListener { task ->
                            dialog.dismiss()
                            if (task.isSuccessful) {
                                makeToast("تم إضافة الموظف")
                                finish()
                            }else{
                                makeToast(getString(R.string.error))
                            }
                        }
                }else{
                    query.addListenerForSingleValueEvent(object:ValueEventListener{
                        override fun onCancelled(p0: DatabaseError) {
                            dialog.dismiss()
                            makeToast(getString(R.string.error))
                        }
                        override fun onDataChange(p0: DataSnapshot) {
                            var find=false
                            for (p1 in p0.children) {
                                if (p1.value.toString() == employeName.text.toString()) {
                                    find = true
                                    break
                                }
                            }
                            if(find) {
                                makeToast("تم إضافة هذا الموظف من قبل")
                                dialog.dismiss()
                            }
                                else {
                                    query.child(Calendar.getInstance().time.toString())
                                        .setValue(employeName.text.toString())
                                        .addOnCompleteListener { task ->
                                            dialog.dismiss()
                                            if (task.isSuccessful) {
                                                makeToast("تم إضافة الموظف")
                                                finish()
                                            } else {
                                                makeToast(getString(R.string.error))
                                            }
                                        }
                                }
                            }
                    })

                }
            }

        })
    }
}
