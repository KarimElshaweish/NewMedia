package com.karim.myapplication.Fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView

import com.karim.myapplication.R
import kotlinx.android.synthetic.main.fragment_defualt__fregmant.*

class Defualt_Fregmant : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
         var v=inflater.inflate(R.layout.fragment_defualt__fregmant, container, false)
        var image=v.findViewById<ImageView>(R.id.dimage)
        var fadeAnimation=AnimationUtils.loadAnimation(context,R.anim.fade)
        image.startAnimation(fadeAnimation)
        return  v
    }




}
