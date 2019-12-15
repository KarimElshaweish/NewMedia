package com.karim.myapplication.Fragments

import android.content.Intent
import android.graphics.*
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ScrollView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.karim.myapplication.Activites.Basket.PhotoAndSoundBasket
import com.karim.myapplication.Activites.Basket.TheaterAndScreensBasket
import com.karim.myapplication.Adapter.BasketRVAdapter
import com.karim.myapplication.Adapter.RVAdapterPkItems
import com.karim.myapplication.Model.TypesItems
import com.karim.myapplication.R
import com.karim.myapplication.Util
import kotlinx.android.synthetic.main.fragment_basket.*
import kotlinx.android.synthetic.main.fragment_basket.view.*
import kotlinx.android.synthetic.main.fragment_montag.view.*
import kotlinx.android.synthetic.main.fragment_montag.view.cashRest_report
import kotlinx.android.synthetic.main.fragment_montag.view.report
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class BasketFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        builder.detectFileUriExposure()
        // Inflate the layout for this fragment
        var view= inflater.inflate(R.layout.fragment_basket, container, false)
        view.soundControl.setOnClickListener{
            startActivity(Intent(context,PhotoAndSoundBasket::class.java))
        }
        view.cvTheater.setOnClickListener{
            startActivity(Intent(context,TheaterAndScreensBasket::class.java))
        }
        return view
    }
}
