package com.karim.myapplication;

import com.karim.myapplication.Model.PhotoGraph;
import com.karim.myapplication.Model.ScreenType;

import java.util.ArrayList;
import java.util.List;

public class Util {
    public static boolean empolyee=false;
    public static List<PhotoGraph>list=new ArrayList<>();
    public static List<PhotoGraph>soundList=new ArrayList<>();
    public static List<ScreenType>listScreen=new ArrayList<>();

    public static void resetPhoto(){
        list=new ArrayList<>();
    }
    public static void resetScreen(){
        listScreen=new ArrayList<>();
    }
    public static void resetSound(){
        soundList=new ArrayList<>();
    }

}
