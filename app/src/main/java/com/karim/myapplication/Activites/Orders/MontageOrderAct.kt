package com.karim.myapplication.Activites.Orders

import android.app.ActionBar
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.karim.myapplication.model.Montag
import com.karim.myapplication.R
import kotlinx.android.synthetic.main.activity_montage_order.*
import java.util.*

class MontageOrderAct : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_montage_order)
        getData()
    }
    private fun getData(){
        var list= mutableListOf<Montag>()
        FirebaseDatabase.getInstance().getReference("Montage")
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(p0: DataSnapshot) {
                    list.clear()
                    for (p1 in p0.children) {
                        if (p1.key.equals(FirebaseAuth.getInstance().currentUser!!.uid)) {
                            for (p2 in p1.children) {
                                var map = p2.value as Map<String, Objects>
                                var work = Montag(
                                    map["name"].toString(),
                                    map["phoneNumber"].toString(),
                                    map["date"].toString(),
                                    map["cashGet"].toString(),
                                    map["cashRest"].toString(),
                                    map["id"].toString()
                                )
                                createTableRow(work)

                            }
                        }
                    }
                }

            })
    }

    private fun createTableRow(work:Montag) {
        var tableRow=TableRow(this)
        tableRow.setLayoutParams(
            ActionBar.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        )

        tableRow.addView(createTextLabel(work.cashRest))
        tableRow.addView(createTextLabel(work.cashGet))
        tableRow.addView(createTextLabel(work.date))
        tableRow.addView(createTextLabel(work.phoneNumber))
        tableRow.addView(createTextLabel(work.name))
        table.addView(
            tableRow, TableLayout.LayoutParams(
                ViewGroup.LayoutParams.FILL_PARENT, //part4
                ViewGroup.LayoutParams.MATCH_PARENT,1f
            )
        )
    }
    private fun createTextLabel(text:String): TextView {
        val tableRowparam = TableRow.LayoutParams()
        tableRowparam.weight=1f
        val textView = TextView(this)
        textView.setBackgroundDrawable(resources.getDrawable(R.drawable.cell_border))
        textView.layoutParams=tableRowparam
        textView.text = text
        textView.gravity= Gravity.CENTER
        textView.setTextColor(Color.BLACK)
        textView.setPadding(5, 5, 5, 5)
        return textView
    }
    fun finish(view: View) {
        finish()
    }
}
