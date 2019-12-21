package com.karim.myapplication.Model

data class TypesPackages(val name:String,val image:Int)

data class TypesItems(val item:String)
data class ScreenType(val price:String,val totalMeter:String,val type:String)
data class WorkDone(val employeName:String,val workURL:String,val workDate:String,val workName:String)
data class Montag(val name: String,val phoneNumber:String,val date:String,val cashGet:String,val cashRest:String)
data class photoData(val items:List<PhotoGraph>,val clientName:String,var moneyGet:String,val phoneNumber: String,val location:String)
