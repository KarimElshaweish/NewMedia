package com.karim.myapplication.Fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import com.karim.myapplication.Slider.CardFragmentPagerAdapter

import com.karim.myapplication.R
import com.karim.myapplication.Slider.ShadowTransformer
import com.yarolegovich.mp.util.Utils


class PhotographerFragmetn : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v= inflater.inflate(R.layout.fragment_photographer_fragmetn, container, false)
        val viewPager = v.findViewById(R.id.viewPager) as ViewPager

        val pagerAdapter =
            CardFragmentPagerAdapter(
                activity?.supportFragmentManager,
                Utils.dpToPixels(context, 9).toFloat()
            ,false)
        val fragmentCardShadowTransformer =
            ShadowTransformer(viewPager, pagerAdapter)
        fragmentCardShadowTransformer.enableScaling(true)

        viewPager.adapter = pagerAdapter
        viewPager.setPageTransformer(false, fragmentCardShadowTransformer)
        viewPager.offscreenPageLimit = 3
        return v
    }


}
