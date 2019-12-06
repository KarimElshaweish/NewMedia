package com.karim.myapplication.Fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.karim.myapplication.Activites.TheaterAndScreens.ScreensActivity
import com.karim.myapplication.Activites.TheaterAndScreens.Theater
import com.karim.myapplication.R

class TheaterAndScreens : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
         var view=inflater.inflate(R.layout.fragment_theater_and_screens, container, false)

        var theater=view.findViewById<CardView>(R.id.theater_control)
        theater.setOnClickListener{
            startActivity(Intent(context,Theater::class.java))
        }


        var screen_control=view.findViewById<CardView>(R.id.screen_control);
        screen_control.setOnClickListener{
            startActivity(Intent(context,ScreensActivity::class.java))
        }
        return  view
    }
}




