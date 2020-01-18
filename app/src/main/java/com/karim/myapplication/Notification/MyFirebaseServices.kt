package com.karim.myapplication.Notification

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService

class MyFirebaseServices : FirebaseInstanceIdService() {
    val firebaseUser=FirebaseAuth.getInstance().currentUser
    override fun onTokenRefresh() {
        super.onTokenRefresh()
        var refreshToken=FirebaseInstanceId.getInstance().token
        if(firebaseUser!=null)
            updateToken(refreshToken!!)
    }

    private fun updateToken(refreshToken: String) {
        var ref=FirebaseDatabase.getInstance().reference
        var query=ref.child("Tokens")
        query.child(firebaseUser!!.uid).setValue(refreshToken)
    }
}