package com.karim.myapplication.Repositry

import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.karim.myapplication.Interfaces.OnBasketDataLoadLisenter
import com.karim.myapplication.model.ScreenUploadData
import com.karim.myapplication.model.TheaterUploadData
import com.karim.myapplication.model.photoData
import java.util.*

class Repo{
    var ref=FirebaseDatabase.getInstance().getReference()
    companion object{
        var dataListener: OnBasketDataLoadLisenter?=null
        var instance:Repo?=null
        fun getInstance(_ctxt:Fragment):Repo?{
            dataListener=_ctxt as OnBasketDataLoadLisenter
            if(instance==null){
                instance=Repo()
            }
            return instance
        }
    }
    fun uploadPictures(pd:photoData){
        val uid=FirebaseAuth.getInstance().currentUser!!.uid
        val time=Calendar.getInstance().time.toString()
        pd.id= "$uid*$time"
        var query=ref.child("photoOrders").child(uid)
            .child(time)
        query.setValue(pd).addOnCompleteListener{
            task ->
            if(task.isSuccessful)
                dataListener?.onPohotAddedSuccess()
            else
                dataListener?.onPhotoAddedFailed()
        }
        query=ref.child("photoMonth/${uid}/${time}")
        query.setValue(pd)
    }
    fun uploadMusic(pd:photoData){
        val uid=FirebaseAuth.getInstance().currentUser!!.uid
        val time=Calendar.getInstance().time.toString()
        val query=ref.child("musicOrdersMonth/${uid}/${time}")
        pd.id="${uid}*${time}"
        query.setValue(pd).addOnCompleteListener{
            task ->
            if(task.isSuccessful)
                dataListener?.onMusiceAddedSuccess()
            else
                dataListener?.onMusiceAddedFailed()
        }
    }
    fun uploadTheater(pd: TheaterUploadData) {
        val uid=FirebaseAuth.getInstance().currentUser!!.uid
        val time=Calendar.getInstance().time.toString()
        var query=ref.child("Theater").child(uid)
            .child(time)
        pd.id="${uid}*${time}"
        query.setValue(pd).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                dataListener?.onTheaterAddedSuccess()
            }else{
                dataListener?.onTheaterAddedFailed()
            }
        }
        query=ref.child("TheaterMonth/${uid}/${time}")
        query.setValue(pd)
    }

    fun uploadScreen(screenData: ScreenUploadData) {
        val uid=FirebaseAuth.getInstance().currentUser!!.uid
        val time=Calendar.getInstance().time.toString()
        screenData.id="${uid}*${time}"
        var query=ref.child("Screen").child(uid).child(
            time
        )
        query.setValue(screenData).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                dataListener?.onScreenAddedSuccess()
            } else {
                dataListener?.onScreenAddedFailed()
            }
        }
        query=ref.child("ScreenMonth/${uid}/${time}")
        query.setValue(screenData)
    }
}