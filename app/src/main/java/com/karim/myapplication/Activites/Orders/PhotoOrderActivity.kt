package com.karim.myapplication.Activites.Orders

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.karim.myapplication.R


class PhotoOrderActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_order)
    }

    fun finish(view: View) {
        finish()
    }
}
