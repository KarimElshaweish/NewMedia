package com.karim.myapplication.Fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.karim.myapplication.Activites.AddWorkDone
import com.karim.myapplication.Adapter.BasketRVAdapter
import com.karim.myapplication.Adapter.workAdapter
import com.karim.myapplication.Model.ScreenType
import com.karim.myapplication.Model.WorkDone
import com.karim.myapplication.R
import com.karim.myapplication.Util
import kotlinx.android.synthetic.main.fragment_work_done.*
import kotlinx.android.synthetic.main.fragment_work_done.view.*
import kotlinx.android.synthetic.main.fragment_work_done.view.rv
import java.util.*

class WorkDoneFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
         var view= inflater.inflate(R.layout.fragment_work_done, container, false)

        view.add_fab.setOnClickListener{
                startActivity(Intent(context,AddWorkDone::class.java))
        }

       view.rv.setHasFixedSize(true)
        view.rv.layoutManager= LinearLayoutManager(context)
        getData()
        return view
    }


    private fun getData(){
        var list= mutableListOf<WorkDone>()
        FirebaseDatabase.getInstance().getReference("WorkDone")
            .addValueEventListener(object :ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(p0: DataSnapshot) {
                    list.clear()
                    for (p1 in p0.children){
                        for(p2 in p1.children) {
                            var map = p2.value as Map<String, Objects>
                            var work = WorkDone(
                                map.get("employeName").toString(),
                                map.get("workDate").toString(),
                                map.get("workURL").toString()
                            )
                            list.add(work)
                        }
                    }
                    var adapter=workAdapter(context!!,list)
                    rv.adapter=adapter
                }

            })
    }


}
