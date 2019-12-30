package com.karim.myapplication.Repositry

import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.karim.myapplication.OnBasketDataLoadLisenter
import com.karim.myapplication.model.ScreenUploadData
import com.karim.myapplication.model.TheaterUploadData
import com.karim.myapplication.model.photoData
import java.util.*

class Repo{
    var ref=FirebaseDatabase.getInstance().getReference()
    companion object{
        var dataListener:OnBasketDataLoadLisenter?=null
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
        var query=ref.child("photoOrders").child(FirebaseAuth.getInstance().currentUser!!.uid)
            .child(Calendar.getInstance().time.toString())
        query.setValue(pd).addOnCompleteListener{
            task ->
            if(task.isSuccessful)
                dataListener?.onPohotAddedSuccess()
            else
                dataListener?.onPhotoAddedFailed()
        }
    }
    fun uploadMusic(pd:photoData){
        var query=ref.child("musicOrders").child(FirebaseAuth.getInstance().currentUser!!.uid)
            .child(Calendar.getInstance().time.toString())
        query.setValue(pd).addOnCompleteListener{
            task ->
            if(task.isSuccessful)
                dataListener?.onMusiceAddedSuccess()
            else
                dataListener?.onMusiceAddedFailed()
        }
    }
    fun uploadTheater(pd: TheaterUploadData) {
        var query=ref.child("Theater").child(FirebaseAuth.getInstance().currentUser!!.uid)
            .child(Calendar.getInstance().time.toString())
        query.setValue(pd).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                dataListener?.onTheaterAddedSuccess()
            }else{
                dataListener?.onTheaterAddedFailed()
            }
        }
    }

    fun uploadScreen(screenData: ScreenUploadData) {
        var query=ref.child("Screen").child(FirebaseAuth.getInstance().currentUser!!.uid).child(
            Calendar.getInstance().time.toString()
        )
        query.setValue(screenData).addOnCompleteListener{
            task ->if(task.isSuccessful) {
            dataListener?.onScreenAddedSuccess()
        }else{
            dataListener?.onScreenAddedFailed()
        }
        }
    }
}