package com.karim.myapplication.Activites.ControlPannel

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.karim.myapplication.Activites.ControlPannel.PhotoGrapherControl.PhotoGrapherAddPackage
import com.karim.myapplication.Activites.ControlPannel.SoundControl.SoundControlAddPackage
import com.karim.myapplication.R
import com.karim.myapplication.Slider.CardFragmentPagerAdapter
import com.karim.myapplication.Slider.ShadowTransformer
import com.yarolegovich.mp.util.Utils
import kotlinx.android.synthetic.main.activity_photo_grapher_control.*

class SoundControlActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sound_control)
        val pagerAdapter =
            CardFragmentPagerAdapter(
                supportFragmentManager,
                Utils.dpToPixels(this, 2).toFloat()
                ,true)
        val fragmentCardShadowTransformer =
            ShadowTransformer(viewPager, pagerAdapter)
        fragmentCardShadowTransformer.enableScaling(true)

        viewPager.adapter = pagerAdapter
        viewPager.setPageTransformer(false, fragmentCardShadowTransformer)
        viewPager.offscreenPageLimit = 3
        add_fab.setOnClickListener{
            startActivity(Intent(this, SoundControlAddPackage::class.java))
        }
    }

    fun finish(view: View) {
        finish()
    }
}
