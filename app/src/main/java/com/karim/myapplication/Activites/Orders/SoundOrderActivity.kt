package com.karim.myapplication.Activites.Orders

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.karim.myapplication.R

class SoundOrderActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sound_order)
    }

    fun finish(view: View) {
        finish()
    }
}
