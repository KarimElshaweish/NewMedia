package com.karim.myapplication.Repositry

import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.karim.myapplication.Interfaces.Report
import com.karim.myapplication.model.ReportData

class ReportRepo {
    var ref=FirebaseDatabase.getInstance().reference
    var reportMontageModel=ArrayList<ReportData>()
    var reportMusicModel=ArrayList<ReportData>()
    var reportPictureModel=ArrayList<ReportData>()
    var reportTheaterModel=ArrayList<ReportData>()
    var reportScreenModel=ArrayList<ReportData>()
    companion object{
       private var listener:Report?=null
        private var instance:ReportRepo?=null
        fun getinstance(_ctx:Fragment):ReportRepo?{
            if(instance==null)
                instance= ReportRepo()
            listener =_ctx as Report
            return instance
        }
    }
    fun getMontageReport():MutableLiveData<ArrayList<ReportData>>{
        if(reportMontageModel.size==0)
            loadreportMontageModel()
        val montage=MutableLiveData<ArrayList<ReportData>>()
        montage.value=reportMontageModel
        return montage
    }
    fun getMusicReport():MutableLiveData<ArrayList<ReportData>>{
        if(reportMusicModel.size==0)
            loadreportMusicModel()
        val music=MutableLiveData<ArrayList<ReportData>>()
        music.value=reportMusicModel
        return music
    }
    fun getPictureReport():MutableLiveData<ArrayList<ReportData>>{
        if(reportMusicModel.size==0)
            loadreportPictureModel()
        val picture=MutableLiveData<ArrayList<ReportData>>()
        picture.value=reportPictureModel
        return picture
    }
    fun getTheaterReport():MutableLiveData<ArrayList<ReportData>>{
        if(reportTheaterModel.size==0)
            loadreportTheaterModel()
        val theater=MutableLiveData<ArrayList<ReportData>>()
        theater.value=reportTheaterModel
        return theater
    }
    fun getScreenReport():MutableLiveData<ArrayList<ReportData>>{
        if(reportScreenModel.size==0)
            loadreportScreenModel()
        val screen=MutableLiveData<ArrayList<ReportData>>()
        screen.value=reportScreenModel
        return screen
    }
    fun loadreportMontageModel(){
        var query=ref.child("Montage")
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                listener!!.onReportMontageMoneyLoadedFailed()
            }
            override fun onDataChange(p0: DataSnapshot) {
                for(p1 in p0.children){
                    for(p2 in p1.children){
                        val map=p2.value as Map<*, *>
                        val cashGet=map["cashGet"].toString()
                        val date=map["date"].toString()
                        reportMontageModel.add(ReportData(date,cashGet))
                    }
                }
                listener!!.onReportMontageMoneyLoadedSuccess()
            }
        })

    }
    fun loadreportMusicModel(){
        var query=ref.child("musicOrdersMonth")
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
               listener!!.onReportMusicMoneyLoadedFailed()
            }
            override fun onDataChange(p0: DataSnapshot) {
                for (p1 in p0.children) {
                    for (p2 in p1.children) {
                        val map = p2.value as Map<*,*>
                        val date=map["date"].toString()
                        reportMusicModel.add(ReportData(date,map["moneyHave"].toString()))
                    }
                }
               listener!!.onReportMusicMoneyLoadedSuccess()
            }
        })

    }
    fun loadreportPictureModel(){
        val query=ref.child("photoMonth")
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                listener!!.onReportPhotoMoneyLoadedFaield()
            }
            override fun onDataChange(p0: DataSnapshot) {
                for (p1 in p0.children) {
                    for (p2 in p1.children) {
                        val map = p2.value as Map<*,*>
                        val date=map["date"].toString()
                        reportPictureModel.add(ReportData(date,map["moneyHave"].toString()))
                    }
                }
               listener!!.onReportPhotoMoneyLoadedSuccess()
            }
        })

    }
    fun loadreportTheaterModel(){
        val query=ref.child("Theater")
        query.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
              listener!!.onReportTheaterMoneyLoadedFaield()
            }
            override fun onDataChange(p0: DataSnapshot) {
                for(p1 in p0.children){
                    for(p2 in p1.children){
                        val map = p2.value as Map<*,*>
                        val date=map["date"].toString()
                        reportTheaterModel.add(ReportData(date,map["moneyHave"].toString()))
                    }
                }
                listener!!.onReportTheaterMoneyLoadedSuccess()
            }
        })

    }
    fun loadreportScreenModel(){
        val query=ref.child("ScreenMonth")
        query.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
               listener!!.onReportScreenMoneyLoadedFaield()
            }
            override fun onDataChange(p0: DataSnapshot) {
                for(p1 in p0.children){
                    for(p2 in p1.children){
                        val map = p2.value as Map<*,*>
                        val date=map["date"].toString()
                        reportScreenModel.add(ReportData(date,map["moneyHave"].toString()))
                    }
                }
               listener!!.onReportScreenMoneyLoadedSuccess()
            }

        })

    }
}