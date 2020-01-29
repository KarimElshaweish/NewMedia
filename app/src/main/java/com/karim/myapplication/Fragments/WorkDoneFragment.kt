package com.karim.myapplication.Fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.widget.AppCompatSpinner
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.karim.myapplication.Activites.AddEmployee
import com.karim.myapplication.Activites.AddWorkDone
import com.karim.myapplication.Adapter.EmployeeAdapter
import com.karim.myapplication.Adapter.workAdapter
import com.karim.myapplication.Interfaces.OnWorkDoneLoadedLisntner
import com.karim.myapplication.model.WorkDone
import com.karim.myapplication.R
import com.karim.myapplication.Util
import com.karim.myapplication.ViewModel.WorkDoneViewModel
import com.karim.myapplication.model.EmployeeData
import kotlinx.android.synthetic.main.fragment_work_done.*
import kotlinx.android.synthetic.main.fragment_work_done.view.*
import kotlinx.android.synthetic.main.fragment_work_done.view.rv
import schemasMicrosoftComOfficeOffice.STInsetMode
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet

class WorkDoneFragment : Fragment(){


    var employeeSpinner: AppCompatSpinner?=null
    var employe=""
    var filterRl:LinearLayout?=null
    var employeesList1= mutableListOf<EmployeeData>()
    var employeAdapter:EmployeeAdapter?=null
    var listEmp= mutableListOf<String>()
    fun getEmployee(){
        val ref =FirebaseDatabase.getInstance().reference
        val query=ref.child("employees")
        query.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(context,getString(R.string.error),Toast.LENGTH_SHORT).show()
            }
            override fun onDataChange(p0: DataSnapshot) {
                employeesList1= ArrayList()
                 listEmp.clear()
                listEmp.add("الكل")
                workDonePb.visibility=View.GONE
                for(p1 in p0.children){
                    val mp=EmployeeData(p1.key.toString(),p1.value.toString())
                    employeesList1.add(mp)
                    listEmp.add(mp.name)
                }
                employeAdapter = EmployeeAdapter(employeesList1,context!!)
                view!!.rv.adapter = employeAdapter
                val employeAdapter= ArrayAdapter(context!!,R.layout.spinner_text_view,listEmp)
                employeeSpinner!!.adapter=employeAdapter
            }

        })
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
         val view= inflater.inflate(R.layout.fragment_work_done, container, false)
        filterRl=view.findViewById(R.id.filterRl)
        if(Util.empolyee)
            view.add_fab!!.visibility=View.GONE
        employeeSpinner=view.findViewById(R.id.employeeSpinner)
        employeeSpinner!!.onItemSelectedListener=object :  AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view1: View?,
                position: Int,
                id: Long
            ) {
                employe=listEmp[position]
                filter(view)
            }

        }
        view.add_fab.setOnClickListener{
                startActivity(Intent(context,AddEmployee::class.java))
        }
        view.rv.setHasFixedSize(true)
        view.rv.layoutManager= LinearLayoutManager(context)
        getEmployee()
        return view
    }

    private fun filter(view: View) {
        when (employe) {
            "الكل" -> {
                employeAdapter = EmployeeAdapter(employeesList1, context!!)
                view.rv.adapter = employeAdapter
            }
            else -> {
                val list = ArrayList<EmployeeData>()
                for (emp in employeesList1) {
                    if (emp.name == employe)
                        list.add(emp)
                }
                employeAdapter = EmployeeAdapter(list, context!!)
                view.rv.adapter = employeAdapter
            }
        }
    }
}
