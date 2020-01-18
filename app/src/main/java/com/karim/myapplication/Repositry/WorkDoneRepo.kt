package com.karim.myapplication.Repositry

import android.content.Context
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.firebase.ui.auth.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.karim.myapplication.Adapter.workAdapter
import com.karim.myapplication.Interfaces.OnWorkDoneLoadedLisntner
import com.karim.myapplication.model.WorkDone
import kotlinx.android.synthetic.main.fragment_work_done.*
import java.util.*
import kotlin.collections.ArrayList

class WorkDoneRepo {
    private var allWorkDoneModel=ArrayList<WorkDone>()
    private var employeeWorkDoneModel=ArrayList<WorkDone>()
    private var employesModel=ArrayList<String>()
    var ref=FirebaseDatabase.getInstance().reference
    companion object{
        var _ctx:Context?=null
        var instance:WorkDoneRepo?=null
        var dataListener:OnWorkDoneLoadedLisntner?=null
        fun getInstnace(_ctx:Fragment):WorkDoneRepo?{
            dataListener=_ctx as OnWorkDoneLoadedLisntner
            if(instance==null) {
                instance = WorkDoneRepo()
            }
            return instance
        }
    }
    fun getAllWordDone(): MutableLiveData<ArrayList<WorkDone>> {
        if(allWorkDoneModel.size==0)
            loadAllWorkDone()
        var works=MutableLiveData<ArrayList<WorkDone>>()
        works.value=allWorkDoneModel
        return works
    }
    fun getEmployees(): MutableLiveData<ArrayList<String>> {
        if(employesModel.size==0)
            loadEmployes()
        var employes=MutableLiveData<ArrayList<String>>()
        employes.value=employesModel
        return employes
    }
    fun getEmployeeWorkDone(uid:String):MutableLiveData<ArrayList<WorkDone>>{
        if(employeeWorkDoneModel.size==0)
            loadEmployeeWorkDone(uid)
        var work= MutableLiveData<ArrayList<WorkDone>>()
        work.value=employeeWorkDoneModel
        return work
    }

    private fun loadEmployes(){
        val query=ref.child("WorkDone")
        query.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                dataListener!!.onEmployesLoadFailed()
            }
            override fun onDataChange(p0: DataSnapshot) {
                for(p1 in p0.children){
                    for(p2 in p1.children){
                        val map = p2.value as Map<*, *>
                        employesModel.add(map["employeName"].toString())
                    }
                }
                dataListener!!.onEmployesLoadSuccess()
            }

        })
    }
    private fun loadEmployeeWorkDone(uid: String) {
        var query=ref.child("WorkDone")
        query.addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {
                    dataListener!!.onEmployeeWorkDoneLoadedFailed()
                }
                override fun onDataChange(p0: DataSnapshot) {
                    for (p1 in p0.children) {
                        if (p1.key.equals(uid)) {
                            for (p2 in p1.children) {
                                var map = p2.value as Map<String, Objects>
                                var work = WorkDone(
                                    map.get("employeName").toString(),
                                    map.get("workDate").toString(),
                                    map.get("workURL").toString(),
                                    map.get("workName").toString()
                                )
                                employeeWorkDoneModel.add(work)
                            }
                        }
                    }
                    dataListener!!.onEmployeeWorkDoneLoadedSuccessfully()
                }

            })

    }

    private fun loadAllWorkDone() {
        var query=ref.child("WorkDone")
        query.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                dataListener!!.onWorkDoneLoadedFaield()
            }
            override fun onDataChange(p0: DataSnapshot) {
                for (p1 in p0.children) {
                        for (p2 in p1.children) {
                            var map = p2.value as Map<String, Objects>
                            var work = WorkDone(
                                map.get("employeName").toString(),
                                map.get("workDate").toString(),
                                map.get("workURL").toString(),
                                map.get("workName").toString()
                            )
                            allWorkDoneModel.add(work)
                    }
                }
                dataListener!!.onWorkDoneLoadSuccefully()
            }

        })
    }
}