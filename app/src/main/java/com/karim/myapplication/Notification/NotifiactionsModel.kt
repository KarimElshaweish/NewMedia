package com.karim.myapplication.Notification

class Token(var token:String) {
    fun Token(){
    }
}
data class MyResponse (var data: Data,var to:String)
data class Data(var user:String,var name:String,var orderDate:String)
data class Sender(var data: Data,var to:String)