package com.karim.myapplication.Repositry

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.*
import com.karim.myapplication.Interfaces.OnMonthilyOrderLoad
import com.karim.myapplication.model.*
import java.util.*
import kotlin.collections.ArrayList

class MonthReop {
    private var ref=FirebaseDatabase.getInstance().getReference()
    private  var photoModel=ArrayList<photoData>()
    private var screenModel=ArrayList<ScreenUploadData>()
    private var theaterModel=ArrayList<TheaterUploadData>()
    private var musicModel=ArrayList<photoData>()
    private var montagModel=ArrayList<Montag>()
    companion object{
        var instance:MonthReop?=null
        var listener:OnMonthilyOrderLoad?=null
        fun getInstance(_ctx:Fragment):MonthReop?{
            listener=_ctx as OnMonthilyOrderLoad
            if(instance==null)
                instance= MonthReop()
            return instance
        }
    }
    fun getPhoto(): MutableLiveData<ArrayList<photoData>> {
        loadPhoto()
        var photoList= MutableLiveData<ArrayList<photoData>>()
        photoList.value=photoModel
        return photoList
    }
    fun getMusic(): MutableLiveData<ArrayList<photoData>> {
        loadMusic()
        var musicList= MutableLiveData<ArrayList<photoData>>()
        musicList.value=musicModel
        return musicList
    }
    fun getTheater():MutableLiveData<ArrayList<TheaterUploadData>>{
        loadTheater()
        var theaterList= MutableLiveData<ArrayList<TheaterUploadData>>()
        theaterList.value=theaterModel
        return theaterList
    }
    fun getMontag():MutableLiveData<ArrayList<Montag>>{
        loadMontag()
        var montagList= MutableLiveData<ArrayList<Montag>>()
        montagList.value=montagModel
        return montagList
    }
    fun getScreen():MutableLiveData<ArrayList<ScreenUploadData>>{
        loadScreen()
        var screenList= MutableLiveData<ArrayList<ScreenUploadData>>()
        screenList.value=screenModel
        return screenList
    }

    private fun loadMontag(){
        var query=ref.child("Montage")
        query.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                listener!!.onMontageFaield()
            }

            override fun onDataChange(p0: DataSnapshot) {
                montagModel.clear()
                for(p1 in p0.children){
                    for(p2 in p1.children){
                        val map=p2.value as Map<*, *>
                        val cashGet=map["cashGet"].toString()
                        val cashRest=map["cashRest"].toString()
                        val date=map["date"].toString()
                        val id=map["id"].toString()
                        val name=map["name"].toString()
                        val phoneNumber=map["phoneNumber"].toString()
                        val montag=Montag(name,phoneNumber,date,cashGet,cashRest,id,map["time"].toString(),map["checked"]as Boolean)
                        montagModel.add(montag)
                    }
                }
                listener!!.onMontagLoaded()
            }

        })
    }
    private fun loadPhoto() {
        var query=ref.child("photoMonth")
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                listener!!.onPictureFailed()
            }
            override fun onDataChange(p0: DataSnapshot) {
                photoModel.clear()
                for (p1 in p0.children) {
                    for (p2 in p1.children) {
                        var map = p2.value as Map<String, Objects>
                        var ob=map.get("items") as java.util.ArrayList<String>
                        var list=getList(ob)
                        var work = photoData(
                            list ,
                            map.get("clientName").toString(),
                            map.get("moneyGet").toString(),
                            map.get("phoneNumber").toString(),map.get("location").toString(),
                            map.get("date").toString(),
                            map.get("moneyRest").toString(),
                            map.get("moneyHave").toString(),
                            map.get("id").toString()
                            ,map.get("workName").toString(),
                            map["time"].toString(),map["checked"]as Boolean)
                        photoModel.add(work)
                    }
                }
                listener!!.onPicutersOrderLoad()
            }
        })
    }
    private fun loadMusic() {
        var query=ref.child("musicOrdersMonth")
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                listener!!.onMusicOrderLoadFailed()
            }
            override fun onDataChange(p0: DataSnapshot) {
                musicModel.clear()
                for (p1 in p0.children) {
                    for (p2 in p1.children) {
                        var map = p2.value as Map<String, Objects>
                        var ob=map.get("items") as java.util.ArrayList<String>
                        var list=getList(ob)
                        var work = photoData(
                            list ,
                            map.get("clientName").toString(),
                            map.get("moneyGet").toString(),
                            map.get("phoneNumber").toString(),map.get("location").toString(),
                            map.get("date").toString(),
                            map.get("moneyRest").toString(),
                            map.get("moneyHave").toString(),
                            map.get("id").toString()
                            ,map.get("workName").toString(),map["time"].toString(),map["checked"]as Boolean)
                        musicModel.add(work)
                    }
                }
                listener!!.onMusicOrderLoad()
            }
        })
    }
    private fun getList(items: java.util.ArrayList<String>):List<PhotoGraph>{
        var list= mutableListOf<PhotoGraph>()
        for(i in 0 until  items.size step 1){
            var map2=items[i] as HashMap<String, Objects>
            var typelist:List<TypesItems>?=null
            if(map2["items"]!=null){
                typelist =getItemsList(map2.get("items")as java.util.ArrayList<String>)
            }
            var phot=
                PhotoGraph(map2.get("name").toString(),map2.get("price").toString(), typelist,map2.get("iamge").toString())
            list.add(phot)
        }

        return list
    }
    private fun getItemsList(items: java.util.ArrayList<String>):List<TypesItems>{
        var list= mutableListOf<TypesItems>()
        for(i in 0 until items.size step 1){
            var map=items[i]as HashMap<String,String>
            var type= TypesItems(map.get("item").toString())
            list.add(type)
        }
        return list
    }
    fun loadTheater(){
        var query=ref.child("TheaterMonth")
        query.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                listener!!.onTheaterloadedFailed()
            }

            override fun onDataChange(p0: DataSnapshot) {
                theaterModel.clear()
                for(p1 in p0.children){
                    for(p2 in p1.children){
                        var map = p2.value as Map<String, Objects>
                        var ob=map.get("theaterList") as java.util.ArrayList<String>
                        var list=getTheaterLsit(ob)
                        var work = TheaterUploadData(
                            list ,
                            map.get("clientName").toString(),
                            map.get("moneyGet").toString(),
                            map.get("phoneNumber").toString(),map.get("location").toString(),
                            map.get("date").toString(),
                            map.get("moneyRest").toString(),
                            map.get("moneyHave").toString(),
                            map.get("id").toString()
                            ,map.get("workName").toString(),
                            map["time"].toString(),map["checked"]as Boolean)
                        theaterModel.add(work)
                    }
                }
                listener!!.onTheaterloadedSuccess()
            }
        })
    }
    private fun getTheaterLsit(items:ArrayList<String>):MutableList<TheaterData>{
        var list= mutableListOf<TheaterData>()
        for(i in 0 until items.size step 1){
            var map2=items[i]as HashMap<String,String>
            var meter=map2.get("price").toString()
            var price=map2.get("totalMeter").toString()
            list.add(TheaterData(meter,price))
        }
        return list
    }
    fun loadScreen(){
        var query=ref.child("ScreenMonth")
        query.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                listener!!.onScreenloadedFailed()
            }
            override fun onDataChange(p0: DataSnapshot) {
                screenModel.clear()
                for(p1 in p0.children){
                    for(p2 in p1.children){
                        var map = p2.value as Map<String, Objects>
                        var ob=map.get("theaterList") as java.util.ArrayList<String>
                        var list=getScreenLsit(ob)
                        var work = ScreenUploadData(
                            list ,
                            map.get("clientName").toString(),
                            map.get("moneyGet").toString(),
                            map.get("phoneNumber").toString(),map.get("location").toString(),
                            map.get("date").toString(),
                            map.get("moneyRest").toString(),
                            map.get("moneyHave").toString(),
                            map.get("id").toString()
                            ,map.get("workName").toString(),
                            map["time"].toString(),map["checked"]as Boolean)
                        screenModel.add(work)
                    }
                }
                listener!!.onScreenloadedSuceess()
            }

        })
    }
    private fun getScreenLsit(items:ArrayList<String>):MutableList<ScreenType>{
        var list= mutableListOf<ScreenType>()
        for(i in 0 until items.size step 1){
            var map2=items[i]as HashMap<String,String>
            var meter=map2.get("price").toString()
            var price=map2.get("totalMeter").toString()
            list.add(ScreenType(price,meter))
        }
        return list
    }
    fun removeScreen(uid:String,time:String){
        val query= ref.child("ScreenMonth/${uid}/${time}")
        query.setValue(null).addOnCompleteListener{
                task ->
            if(task.isSuccessful){
                listener!!.onScreenRemovedSuccess()
            }else{
                listener!!.onScreenRemovedFailed()
            }
        }
    }
    fun removeMontage(uid: String, time: String) {
        val query = ref.child("Montage/${uid}/${time}")
        query.setValue(null).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                listener!!.onMontagRemovedSuccessfully()
            } else {
                listener!!.onMontagRemovedFailed()
            }
        }
    }
    fun removeTheater(uid:String,time:String) {
        val query = ref.child("TheaterMonth/${uid}/${time}")
        query.setValue(null).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                listener!!.onTheaterRemovedSuccess()
            } else {
                listener!!.onTheaterRemovedFailed()
            }
        }
    }
    fun removeMusic(uid: String, time: String) {
        val query = ref.child("musicOrdersMonth/${uid}/${time}")
        query.setValue(null).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                listener!!.onMusicRemovedSuccessfully()
            } else {
                listener!!.onMusicRemovedFailed()
            }
        }
    }
    fun removePhoto(uid: String, time: String) {
        var query: DatabaseReference?
        query =
            ref.child("photoMonth/${uid}/${time}")
        query.setValue(null).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                listener!!.onphotoRemoveSuccess()
            } else {
                listener!!.onPhotoRemoveFailed()
            }
        }
    }

    fun updateTheter(uid: String, time: String, checked: Boolean) {
        var query: DatabaseReference?
        query =
            ref.child("TheaterMonth/${uid}/${time}/checked")
        query.setValue(checked).addOnCompleteListener { task ->
            if (task.isSuccessful) {

            } else {
            }
        }
    }

    fun updateScreen(uid: String, time: String, checked: Boolean) {
        var query: DatabaseReference?
        query =
            ref.child("ScreenMonth/${uid}/${time}/checked")
        query.setValue(checked)
    }

    fun updatePhoto(uid: String, time: String, checked: Boolean) {
        var query: DatabaseReference?
        query =
            ref.child("photoMonth/${uid}/${time}/checked")
        query.setValue(checked)
    }

    fun updateMusic(uid: String, time: String, checked: Boolean) {
        var query: DatabaseReference?
        query =
            ref.child("musicOrdersMonth/${uid}/${time}/checked")
        query.setValue(checked)
    }

    fun updateMontage(uid: String, time: String, checked: Boolean) {
        var query: DatabaseReference?
        query =
            ref.child("Montage/${uid}/${time}/checked")
        query.setValue(checked)
    }
}