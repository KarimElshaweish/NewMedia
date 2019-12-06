package com.karim.myapplication.Fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.karim.myapplication.Activites.ControlPannel.PhotoGrapherControlActivity
import com.karim.myapplication.Activites.ControlPannel.SoundControlActivity
import com.karim.myapplication.R
import kotlinx.android.synthetic.main.fragment_control_panel.*


class ControlPanelFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view:View
        view= inflater.inflate(R.layout.fragment_control_panel, container, false)

var rlPhoto=view.findViewById<CardView>(R.id.photograpger_control)
        rlPhoto.setOnClickListener{
            startActivity(Intent(context,PhotoGrapherControlActivity::class.java))
        }

        var rlSound=view.findViewById<CardView>(R.id.soundControl)
        rlSound.setOnClickListener{
            startActivity(Intent(context,SoundControlActivity::class.java))
        }

        return view
    }


}
