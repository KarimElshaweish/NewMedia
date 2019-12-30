package com.karim.myapplication.ViewModel

import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.karim.myapplication.Repositry.OrderRepo
import com.karim.myapplication.model.ScreenUploadData
import com.karim.myapplication.model.TheaterData
import com.karim.myapplication.model.TheaterUploadData
import com.karim.myapplication.model.photoData

class OrdersViewModel:ViewModel() {
    lateinit var soundList:MutableLiveData<ArrayList<photoData>>
     lateinit var photList:MutableLiveData<ArrayList<photoData>>
    lateinit var screenList:MutableLiveData<ArrayList<ScreenUploadData>>
    lateinit var theaterList:MutableLiveData<ArrayList<TheaterUploadData>>
    fun init(_ctx:Fragment){
        var repo=OrderRepo.getInstance(_ctx)!!
//        if(!::soundList.isInitialized)
//            soundList=repo.getSound()
        if(!::photList.isInitialized)
            photList=repo.getPhoto()
        if(!::screenList.isInitialized)
            screenList=repo.getScreen()
        if(!::theaterList.isInitialized)
            theaterList=repo.getTheater()

    }
    fun getPhoto()=photList
    fun getSound()=soundList
    fun getScreen()=screenList
    fun getTheater()=theaterList
}