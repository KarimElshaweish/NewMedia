package com.karim.myapplication.model

data class TypesPackages(val name:String,val image:Int)

data class TypesItems(val item:String)
data class ScreenType(var price:String,var totalMeter:String)
data class WorkDone(val employeName:String,val workURL:String,val workDate:String,val workName:String)
data class Montag(val name: String,val phoneNumber:String,val date:String,val cashGet:String,val cashRest:String)
    data class photoData(var items:List<PhotoGraph>, var clientName:String, var moneyGet:String, var phoneNumber: String
                         , var location:String,var date:String,
                         var moneyRest:String,var moneyHave:String)
data class BasketData(var name:String,var moneyGet: String,var phoneNumber: String,var location: String,var date: String,var soundGet:String,
                      var soundRest:String,var photoGet:String,var photoRest:String,var theaterGet:String,var theaterRest:String,
                      var screenGet:String,var screenRest:String)
data class TheaterData(var totalMeter:String,var price: String)
data class TheaterUploadData(var theaterList:MutableList<TheaterData>
                                , var clientName:String, var moneyGet:String, var phoneNumber: String
                     , var location:String,var date:String,
                     var moneyRest:String,var moneyHave:String)
data class ScreenUploadData(var theaterList:MutableList<ScreenType>
                                , var clientName:String, var moneyGet:String, var phoneNumber: String
                     , var location:String,var date:String,
                     var moneyRest:String,var moneyHave:String)