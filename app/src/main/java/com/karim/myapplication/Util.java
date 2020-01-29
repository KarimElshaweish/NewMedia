package com.karim.myapplication;

import com.karim.myapplication.Activites.TheaterAndScreens.Theater;
import com.karim.myapplication.model.BasketData;
import com.karim.myapplication.model.OrderData;
import com.karim.myapplication.model.PhotoGraph;
import com.karim.myapplication.model.ScreenType;
import com.karim.myapplication.model.TheaterData;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Util {
    public static boolean empolyee=false;
    public static List<PhotoGraph>list=new ArrayList<>();
    public static List<PhotoGraph>soundList=new ArrayList<>();
    public static List<ScreenType>listScreen=new ArrayList<>();
    public static List<TheaterData>theaterList=new ArrayList<>();
    @NotNull
    public static BasketData basketObj;
    @NotNull
    public static OrderData orderDataObject;

    public static void resetPhoto(){
        list=new ArrayList<>();
    }
    public static void resetScreen(){
        listScreen=new ArrayList<>();
    }
    public static void resetSound(){
        soundList=new ArrayList<>();
    }
    public static boolean photoBolean=false;
    public static boolean musicBolean=false;
    public static boolean theaterBolean=false;
    public static boolean screenBolean=false;

}
