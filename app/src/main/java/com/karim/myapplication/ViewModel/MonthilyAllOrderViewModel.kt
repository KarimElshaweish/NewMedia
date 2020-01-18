package com.karim.myapplication.ViewModel

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.karim.myapplication.Repositry.MonthReop
import com.karim.myapplication.Repositry.OrderRepo
import com.karim.myapplication.model.Montag
import com.karim.myapplication.model.ScreenUploadData
import com.karim.myapplication.model.TheaterUploadData
import com.karim.myapplication.model.photoData
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MonthilyAllOrderViewModel :ViewModel() {
    lateinit var photList: MutableLiveData<ArrayList<photoData>>
    lateinit var musicList: MutableLiveData<ArrayList<photoData>>
    lateinit var screenList: MutableLiveData<ArrayList<ScreenUploadData>>
    lateinit var theaterList: MutableLiveData<ArrayList<TheaterUploadData>>
    lateinit var montagList:MutableLiveData<ArrayList<Montag>>
    var hashMap : TreeMap<Date, Any> = TreeMap()
    var repo:MonthReop?=null

    fun init(_ctx: Fragment){
        repo= MonthReop.getInstance(_ctx)!!
        if(!::photList.isInitialized)
            photList=repo!!.getPhoto()
        if(!::screenList.isInitialized)
            screenList=repo!!.getScreen()
        if(!::theaterList.isInitialized)
            theaterList=repo!!.getTheater()
        if(!::musicList.isInitialized)
            musicList=repo!!.getMusic()
        if(!::montagList.isInitialized)
            montagList=repo!!.getMontag()

    }
    fun getPhoto()=photList
    fun getScreen()=screenList
    fun getTheater()=theaterList
    fun getMusic()=musicList
    fun  getmontag()=montagList

}