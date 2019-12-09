package com.karim.myapplication.Model

data class TypesPackages(val name:String,val image:Int)

data class TypesItems(val item:String)

data class PhotoGraph(
    val name:String,
    val Price:String,
    val items: ArrayList<TypesItems>
)