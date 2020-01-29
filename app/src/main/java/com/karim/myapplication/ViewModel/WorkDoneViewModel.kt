package com.karim.myapplication.ViewModel

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.karim.myapplication.Repositry.WorkDoneRepo
import com.karim.myapplication.model.WorkDone

class   WorkDoneViewModel:ViewModel() {
 //   lateinit var allWorkDone:MutableLiveData<ArrayList<WorkDone>>
    lateinit var employeeWorkDone : MutableLiveData<ArrayList<WorkDone>>
    fun init(_ctx:Context,name:String){
//        if(!::allWorkDone.isInitialized)
//            allWorkDone=WorkDoneRepo.getInstnace(_ctx)!!.getAllWordDone()
        if(!::employeeWorkDone.isInitialized)
            employeeWorkDone=WorkDoneRepo.getInstnace(_ctx)!!.getEmployeeWorkDone(name)
    }
   // fun getAllWordDone()=allWorkDone
    fun getEmployeeWorkDoneList()=employeeWorkDone
}