package com.karim.myapplication.ViewModel

import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.karim.myapplication.Repositry.ReportRepo
import com.karim.myapplication.model.ReportData

class ReportViewModel: ViewModel() {
    lateinit var montageReportLsit:MutableLiveData<ArrayList<ReportData>>
    lateinit var picuterReportLsit:MutableLiveData<ArrayList<ReportData>>
    lateinit var musicReportLsit:MutableLiveData<ArrayList<ReportData>>
    lateinit var screenReportLsit:MutableLiveData<ArrayList<ReportData>>
    lateinit var theaterReportLsit:MutableLiveData<ArrayList<ReportData>>
    var repo:ReportRepo?=null
    fun init(_ctx:Fragment){
        repo=ReportRepo.getinstance(_ctx)
        if(!::montageReportLsit.isInitialized)
        montageReportLsit=repo!!.getMontageReport()
        if(!::picuterReportLsit.isInitialized)
        picuterReportLsit=repo!!.getPictureReport()
        if(!::musicReportLsit.isInitialized)
        musicReportLsit=repo!!.getMusicReport()
        if(!::screenReportLsit.isInitialized)
        screenReportLsit=repo!!.getScreenReport()
        if(!::theaterReportLsit.isInitialized)
        theaterReportLsit=repo!!.getTheaterReport()
    }
}