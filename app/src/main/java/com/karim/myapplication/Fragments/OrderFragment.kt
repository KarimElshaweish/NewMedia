package com.karim.myapplication.Fragments

import android.app.ActionBar
import android.content.Intent
import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.karim.myapplication.Activites.Orders.MontageOrderAct
import com.karim.myapplication.Activites.Orders.PhotoOrderActivity
import com.karim.myapplication.Model.Montag
import com.karim.myapplication.Model.PhotoGraph
import com.karim.myapplication.Model.TypesItems
import com.karim.myapplication.Model.photoData
import com.karim.myapplication.R
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.activity_montage_order.*
import kotlinx.android.synthetic.main.fragment_order.view.*
import java.util.*

class OrderFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
         var view= inflater.inflate(R.layout.fragment_order, container, false)
        view.montag_control.setOnClickListener{
            startActivity(Intent(context,MontageOrderAct::class.java))
        }
        view.photograpger_control.setOnClickListener{
            startActivity(Intent(context,PhotoOrderActivity::class.java))
        }
        getData(view)

        return view
    }

    private fun getData(view:View){
        val dialog: android.app.AlertDialog? = SpotsDialog.Builder()
            .setContext(context)
            .setTheme(R.style.getData)
            .build()
        var list= mutableListOf<Montag>()
        dialog!!.show()
        FirebaseDatabase.getInstance().getReference().addValueEventListener(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
            override fun onDataChange(p0: DataSnapshot) {
                if(p0.hasChild("PhotoData")){
                    FirebaseDatabase.getInstance().getReference("PhotoData")
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
                                            var ob=map.get("items") as ArrayList<String>
                                            var list=getList(ob)
                                            var work = photoData(

                                                list ,
                                                map.get("clientName").toString(),
                                                map.get("moneyGet").toString(),
                                                "",""
                                            )
                                            createTableRow(work,view)

                                        }
                                    }
                                }
                                dialog!!.dismiss()
                            }

                        })
                }else
                    dialog.dismiss()
            }

        })

    }

    private fun getList(items: ArrayList<String>):List<PhotoGraph>{
        var list= mutableListOf<PhotoGraph>()
        for(i in 0 until  items.size step 1){
            var map2=items[i] as HashMap<String, Objects>
            var typelist=getItemsList(map2.get("items") as ArrayList<String>)
            var phot=
                PhotoGraph(map2.get("name").toString(),map2.get("price").toString(), typelist,map2.get("iamge").toString())
            list.add(phot)
        }

        return list
    }
    private fun getItemsList(items:ArrayList<String>):List<TypesItems>{
        var list= mutableListOf<TypesItems>()
        for(i in 0 until items.size step 1){
            var map=items[i]as HashMap<String,String>
            var type= TypesItems(map.get("item").toString())
            list.add(type)
        }
        return list
    }
    var max=0
    private fun createTableRow(work: photoData,view:View) {

        var itemStr=""
        for(item in work.items){
            for(i in item.items) {
                itemStr +=i.item+"\n"
            }
            max= heightcheck(itemStr)
        }
        itemStr=""
        for(item in work.items){
            var tableRow= TableRow(context!!)
            tableRow.setLayoutParams(
                ActionBar.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            )
            tableRow.addView(createTextLabel(work.moneyGet))
            for(i in item.items) {
                itemStr +=i.item+" -"
            }
            tableRow.addView(createTextLabel(itemStr))
            tableRow.addView(createTextLabel(item.price))
            tableRow.addView(createTextLabel(work.clientName))
            table.addView(tableRow)
        }


    }

    private fun heightcheck(text: String):Int {
        val tableRowparam = TableRow.LayoutParams()
        tableRowparam.weight=1f
        val textView = TextView(context!!)
        textView.setBackgroundDrawable(resources.getDrawable(R.drawable.cell_border))
        textView.layoutParams=tableRowparam
        textView.text = text
        textView.gravity= Gravity.CENTER
        textView.setTextColor(Color.BLACK)
        textView.setPadding(5, 5, 5, 5)


        val bounds = Rect()
        val textPaint = textView.paint
        textPaint.getTextBounds(text, 0, textView.length(), bounds)
        val height = bounds.height()
        val width = bounds.width()
        return height
    }

    private fun createTextLabel(text:String): TextView {
        val tableRowparam = TableRow.LayoutParams()
        tableRowparam.weight=1f
        val textView = TextView(context)
        textView.height=max*4+10
        textView.setBackgroundDrawable(resources.getDrawable(R.drawable.cell_border))
        textView.layoutParams=tableRowparam
        textView.text = text
        textView.gravity= Gravity.CENTER
        textView.setTextColor(Color.BLACK)
        textView.setPadding(5, 5, 5, 5)
        return textView
    }
}
