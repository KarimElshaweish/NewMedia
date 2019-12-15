package com.karim.myapplication.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.karim.myapplication.Adapter.CardAdapter
import com.karim.myapplication.Model.PhotoGraph
import com.karim.myapplication.Model.TypesItems
import com.karim.myapplication.R
import com.yarolegovich.discretescrollview.DiscreteScrollView
import com.yarolegovich.discretescrollview.transform.Pivot
import com.yarolegovich.discretescrollview.transform.ScaleTransformer
import dmax.dialog.SpotsDialog
import java.util.*


class SoundFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_sound, container, false)
        getData(view)
        return view
    }

    var count:Int = 0
    val photoList = mutableListOf<PhotoGraph>()
    private fun getData(v:View){

        val dialog: android.app.AlertDialog? = SpotsDialog.Builder()
            .setContext(context)
            .setTheme(R.style.getData)
            .build()
        dialog!!.show()

        FirebaseDatabase.getInstance().getReference("Sound").addListenerForSingleValueEvent(object :
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
                    setUI(v,count)
                }
            }

        })
    }

    private fun setUI(v:View,count:Int) {
        var rv=v.findViewById<RecyclerView>(R.id.picker) as DiscreteScrollView
        rv.setItemTransformer(
            ScaleTransformer.Builder()
                .setMaxScale(1.05f)
                .setMinScale(0.8f)
                .setPivotX(Pivot.X.CENTER)
                .setPivotY(Pivot.Y.BOTTOM)
                .build())
        var cardAdapter= CardAdapter(photoList,context!!,false)
        rv.adapter=cardAdapter
    }
}
