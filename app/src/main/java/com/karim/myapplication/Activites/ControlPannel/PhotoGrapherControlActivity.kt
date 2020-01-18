package com.karim.myapplication.Activites.ControlPannel

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.karim.myapplication.Activites.ControlPannel.PhotoGrapherControl.PhotoGrapherAddPackage
import com.karim.myapplication.Adapter.CardAdapter
import com.karim.myapplication.model.PhotoGraph
import com.karim.myapplication.model.TypesItems
import com.karim.myapplication.R
import com.yarolegovich.discretescrollview.DiscreteScrollView
import com.yarolegovich.discretescrollview.transform.Pivot
import com.yarolegovich.discretescrollview.transform.ScaleTransformer
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.activity_photo_grapher_control.*
import java.util.*
import kotlin.collections.HashMap

class PhotoGrapherControlActivity : AppCompatActivity() {
    var dialog: android.app.AlertDialog? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_grapher_control)
        dialog= SpotsDialog.Builder()
            .setContext(this)
            .setTheme(R.style.getData)
            .build()
        add_fab.setOnClickListener{
            startActivity(Intent(this,PhotoGrapherAddPackage::class.java))
        }
        getData()
    }

    fun finish(view: View) {
        finish()
    }
    var count:Int = 0
    val photoList = mutableListOf<PhotoGraph>()
    private fun getData(){
        dialog!!.show()
        FirebaseDatabase.getInstance().getReference().addValueEventListener(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(baseContext,p0.message,Toast.LENGTH_LONG).show()
            }
            override fun onDataChange(p0: DataSnapshot) {
                dialog!!.show()
               if(p0.hasChild("photoGraph")){
                   noPk.visibility=View.GONE
                   photoList.clear()
                   FirebaseDatabase.getInstance().getReference("photoGraph").addValueEventListener(object :
                       ValueEventListener {
                       override fun onCancelled(p0: DatabaseError) {
                           TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                       }

                       override fun onDataChange(p0: DataSnapshot) {
                           count= p0.childrenCount.toInt()
                           if(p0!!.exists()) {
                               for (p1 in p0.children) {
                                   var pp=p1.value as HashMap<String, Objects>
                                   var name:String=pp.get("name")as String
                                   var price:String=pp.get("price")as String
                                   var pkItems=pp.get("items")as MutableList<TypesItems>
                                   var pkImage=pp.get("image")as String
                                   var photoGraph= PhotoGraph(name,price,pkItems,pkImage)
                                   photoList.add(photoGraph)
                                   print(pp)
                               }
                               dialog!!.dismiss()
                               setUI(count)
                           }
                       }

                   })
               }else{
                   dialog!!.dismiss()
                   noPk.visibility=View.VISIBLE
               }
            }

        })

    }

    private fun setUI(count:Int) {
        var rv=findViewById<RecyclerView>(R.id.picker) as DiscreteScrollView
        rv.setItemTransformer(
            ScaleTransformer.Builder()
                .setMaxScale(1.05f)
                .setMinScale(0.8f)
                .setPivotX(Pivot.X.CENTER)
                .setPivotY(Pivot.Y.BOTTOM)
                .build())
        var cardAdapter= CardAdapter(photoList,this,true,"photoGraph")
        rv.adapter=cardAdapter
    }

}
