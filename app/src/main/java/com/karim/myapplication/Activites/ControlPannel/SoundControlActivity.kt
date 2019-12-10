package com.karim.myapplication.Activites.ControlPannel

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.karim.myapplication.R

class SoundControlActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sound_control)
    }

    fun finish(view: View) {
        finish()
    }
}
