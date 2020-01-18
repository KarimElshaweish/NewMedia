package com.karim.myapplication.Fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.karim.myapplication.Activites.AddWorkDone
import com.karim.myapplication.Adapter.workAdapter
import com.karim.myapplication.Interfaces.OnWorkDoneLoadedLisntner
import com.karim.myapplication.model.WorkDone
import com.karim.myapplication.R
import com.karim.myapplication.Util
import com.karim.myapplication.ViewModel.WorkDoneViewModel
import kotlinx.android.synthetic.main.fragment_work_done.*
import kotlinx.android.synthetic.main.fragment_work_done.view.*
import kotlinx.android.synthetic.main.fragment_work_done.view.rv
import java.util.*

class WorkDoneFragment : Fragment(),OnWorkDoneLoadedLisntner {


    var workDoneModel:WorkDoneViewModel= WorkDoneViewModel()
     lateinit var adapter:workAdapter
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
        workDoneModel=ViewModelProviders.of(this).get(WorkDoneViewModel::class.java)
        if(!Util.empolyee) {
            workDoneModel.init(this,"")
            adapter = workAdapter(context!!, workDoneModel.getAllWordDone().value!!)
            view.rv.adapter = adapter
        }else{
            workDoneModel.init(this,FirebaseAuth.getInstance().currentUser!!.uid)
            adapter = workAdapter(context!!, workDoneModel.getEmployeeWorkDoneList().value!!)
            view.rv.adapter = adapter
        }
        return view
    }
    override fun onWorkDoneLoadSuccefully() {
        workDoneModel.allWorkDone.observe(this,Observer<Any>{
            workDonePb.visibility=View.GONE
            adapter.notifyDataSetChanged()
        })
    }

    override fun onWorkDoneLoadedFaield() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onEmployeeWorkDoneLoadedSuccessfully() {
        workDoneModel.getEmployeeWorkDoneList().observe(this,
            Observer<Any>{
                workDonePb.visibility=View.GONE
                adapter.notifyDataSetChanged()
            })
    }

    override fun onEmployeeWorkDoneLoadedFailed() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onEmployesLoadSuccess() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onEmployesLoadFailed() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}
