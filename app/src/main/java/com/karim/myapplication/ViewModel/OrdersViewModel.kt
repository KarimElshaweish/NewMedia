package com.karim.myapplication.ViewModel

import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.karim.myapplication.Repositry.OrderRepo
import com.karim.myapplication.model.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class OrdersViewModel:ViewModel() {
    lateinit var soundList:MutableLiveData<ArrayList<photoData>>
     lateinit var photList:MutableLiveData<ArrayList<photoData>>
    lateinit var screenList:MutableLiveData<ArrayList<ScreenUploadData>>
    lateinit var theaterList:MutableLiveData<ArrayList<TheaterUploadData>>
     var allOrderData=MutableLiveData<ArrayList<OrderData>>()
    var repo:OrderRepo?=null
    fun init(_ctx:Fragment){
        repo=OrderRepo.getInstance(_ctx)!!
//        if(!::soundList.isInitialized)
//            soundList=repo.getSound()
        if(!::photList.isInitialized)
            photList=repo!!.getPhoto()
        if(!::screenList.isInitialized)
            screenList=repo!!.getScreen()
        if(!::theaterList.isInitialized) {
         //   var orderTheater=ArrayList<OrderData>()
            theaterList = repo!!.getTheater()

//            if(orderTheater.size!=0)
//            allOrderData.value!!.addAll(orderPhoto)
        }

    }
    fun getPhoto()=photList
    fun getSound()=soundList
    fun getScreen()=screenList
    fun getTheater()=theaterList
    fun getll():MutableLiveData<ArrayList<OrderData>>{

        return allOrderData
    }
    private fun getDateFromString(date: String): Date? {
        var foramt="EEEE- dd/MM/yyyy"
        val formatter= SimpleDateFormat(foramt,Locale("ar"))
        return formatter.parse(date)
    }

    fun removePhoto(photoData: photoData) {
        var split=photoData.id.split("*").toTypedArray()
        val uid=split[0]
        val time=split[1]
        repo!!.removePhoto(uid,time)
    }
    fun removePhoto(id:String) {
        var split=id.split("*").toTypedArray()
        val uid=split[0]
        val time=split[1]
        repo!!.removePhoto(uid,time)
    }
    fun removeTheater(theater:TheaterUploadData){
        var split=theater.id.split("*").toTypedArray()
        val uid=split[0]
        val time=split[1]
        repo!!.removeTheater(uid,time)
    }
    fun removeTheater(id:String){
        var split=id.split("*").toTypedArray()
        val uid=split[0]
        val time=split[1]
        repo!!.removeTheater(uid,time)
    }
    fun removeScreen(screen:ScreenUploadData){
        var split=screen.id.split("*").toTypedArray()
        var uid=split[0]
        var time=split[1]
        repo!!.removeScreen(uid,time)
    }
    fun removeScreen(id:String){
        var split=id.split("*").toTypedArray()
        var uid=split[0]
        var time=split[1]
        repo!!.removeScreen(uid,time)
    }
}