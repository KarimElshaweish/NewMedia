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
    fun removeScreen(id:String){
        var split=id.split("*").toTypedArray()
        var uid=split[0]
        var time=split[1]
        repo!!.removeScreen(uid,time)
    }
    fun remoMontage(id: String) {
        var split=id.split("*").toTypedArray()
        val uid=split[0]
        val time=split[1]
        repo!!.removeMontage(uid,time)
    }
    fun removeTheater(id:String){
        var split=id.split("*").toTypedArray()
        val uid=split[0]
        val time=split[1]
        repo!!.removeTheater(uid,time)
    }
    fun removeMusic(id:String){
        var split=id.split("*").toTypedArray()
        val uid=split[0]
        val time=split[1]
        repo!!.removeMusic(uid,time)
    }
    fun removePhoto(id:String) {
        var split=id.split("*").toTypedArray()
        val uid=split[0]
        val time=split[1]
        repo!!.removePhoto(uid,time)
    }

    fun updateTheaterTheater(id: String, checked: Boolean) {
        var split=id.split("*").toTypedArray()
        val uid=split[0]
        val time=split[1]
        repo!!.updateTheter(uid,time,checked)
    }

    fun updateScreen(id: String, checked: Boolean) {
        var split=id.split("*").toTypedArray()
        val uid=split[0]
        val time=split[1]
        repo!!.updateScreen(uid,time,checked)
    }

    fun updatePhoto(id: String, checked: Boolean) {
        var split=id.split("*").toTypedArray()
        val uid=split[0]
        val time=split[1]
        repo!!.updatePhoto(uid,time,checked)
    }

    fun updateMusic(id: String, checked: Boolean) {
        var split=id.split("*").toTypedArray()
        val uid=split[0]
        val time=split[1]
        repo!!.updateMusic(uid,time,checked)
    }

    fun updateMontage(id: String, checked: Boolean) {
        var split=id.split("*").toTypedArray()
        val uid=split[0]
        val time=split[1]
        repo!!.updateMontage(uid,time,checked)
    }
}