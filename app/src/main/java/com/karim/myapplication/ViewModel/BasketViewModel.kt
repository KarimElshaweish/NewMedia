package com.karim.myapplication.ViewModel

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.karim.myapplication.Repositry.Repo
import com.karim.myapplication.model.ScreenUploadData
import com.karim.myapplication.model.TheaterUploadData
import com.karim.myapplication.model.photoData

class BasketViewModel:ViewModel() {
    private var repo:Repo?=null
    fun init(_ctx:Fragment):Repo?{
        if(repo==null)
            repo= Repo.getInstance(_ctx)
        return repo
    }
    fun uploadPhotoData(pd:photoData){
        repo?.uploadPictures(pd)
    }
    fun uploadMusice(pd: photoData){
        repo?.uploadMusic(pd)
    }
    fun uploadTheater(pd:TheaterUploadData){
        repo?.uploadTheater(pd)
    }

    fun uploadScreen(screenData: ScreenUploadData) {
        repo?.uploadScreen(screenData)
    }
}