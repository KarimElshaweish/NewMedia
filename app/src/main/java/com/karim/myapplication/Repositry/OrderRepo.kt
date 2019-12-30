package com.karim.myapplication.Repositry

import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.karim.myapplication.Activites.TheaterAndScreens.Theater
import com.karim.myapplication.OnOrdersDataLoaderLisenter
import com.karim.myapplication.model.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class OrderRepo {
    private var ref=FirebaseDatabase.getInstance().getReference()
   private  var photoModel=ArrayList<photoData>()
   private  var soundModel=ArrayList<photoData>()
    private var screenModel=ArrayList<ScreenUploadData>()
    private var theaterModel=ArrayList<TheaterUploadData>()
    companion object{
        var instance: OrderRepo? = null
        var listener:OnOrdersDataLoaderLisenter?=null
        fun getInstance(_ctx: Fragment): OrderRepo? {
            listener=_ctx as OnOrdersDataLoaderLisenter
            if (instance == null) instance = OrderRepo()
            return instance
        }
    }
    fun getPhoto():MutableLiveData<ArrayList<photoData>>{
        loadPhoto()
        var photoList=MutableLiveData<ArrayList<photoData>>()
        photoList.value=photoModel
        return photoList
    }

    fun getSound():MutableLiveData<ArrayList<photoData>>{
        laodSound()
        var soundList=MutableLiveData<ArrayList<photoData>>()
        soundList.value=soundModel
        return soundList
    }
    fun getScreen():MutableLiveData<ArrayList<ScreenUploadData>>{
        loadScreen()
        var screenList= MutableLiveData<ArrayList<ScreenUploadData>>()
        screenList.value=screenModel
        return screenList
    }
    fun getTheater():MutableLiveData<ArrayList<TheaterUploadData>>{
        loadTheater()
        var theaterList= MutableLiveData<ArrayList<TheaterUploadData>>()
        theaterList.value=theaterModel
        return theaterList
    }
    fun loadTheater(){
        var query=ref.child("Theater")
        query.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                listener?.onTheaterloadedFailed()
            }

            override fun onDataChange(p0: DataSnapshot) {
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
                            map.get("moneyHave").toString()
                        )
                        theaterModel.add(work)
                        listener?.onTheaterloadedSuccess()
                    }
                }
            }

        })
    }
    fun loadScreen(){
        var query=ref.child("Screen")
        query.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                listener?.onScreenloadedFailed()
            }

            override fun onDataChange(p0: DataSnapshot) {
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
                            map.get("moneyHave").toString()
                        )
                        screenModel.add(work)
                        listener?.onScreenloadedSuceess()
                    }
                }
            }

        })
    }
    private fun laodSound() {
        soundModel.clear()
        var query=ref.child("musicOrders")
        query.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                listener!!.onMusicFailed()
            }

            override fun onDataChange(p0: DataSnapshot) {
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
                            map.get("moneyHave").toString()
                        )
                        soundModel.add(work)
                    }
                }
                listener!!.onMusicOrderLoad()
            }

        })
    }
    private fun loadPhoto() {
        photoModel.clear()
        var query=ref.child("photoOrders")
        query.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                listener?.onPictureFailed()
            }
            override fun onDataChange(p0: DataSnapshot) {
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
                                map.get("moneyHave").toString()
                            )
                            photoModel.add(work)
                    }
                }
                listener!!.onPicutersOrderLoad()
            }
        })
    }
    private fun getList(items: java.util.ArrayList<String>):List<PhotoGraph>{
        var list= mutableListOf<PhotoGraph>()
        for(i in 0 until  items.size step 1){
            var map2=items[i] as HashMap<String, Objects>
            var typelist=getItemsList(map2.get("items") as java.util.ArrayList<String>)
            var phot=
                PhotoGraph(map2.get("name").toString(),map2.get("price").toString(), typelist,map2.get("iamge").toString())
            list.add(phot)
        }

        return list
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
    private fun getItemsList(items: java.util.ArrayList<String>):List<TypesItems>{
        var list= mutableListOf<TypesItems>()
        for(i in 0 until items.size step 1){
            var map=items[i]as HashMap<String,String>
            var type= TypesItems(map.get("item").toString())
            list.add(type)
        }
        return list
    }
}