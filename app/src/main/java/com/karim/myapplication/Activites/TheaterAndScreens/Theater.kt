package com.karim.myapplication.Activites.TheaterAndScreens

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.karim.myapplication.R
import kotlinx.android.synthetic.main.activity_photo_grapher_add_package.*
import kotlinx.android.synthetic.main.activity_photo_grapher_add_package.price
import kotlinx.android.synthetic.main.activity_theater.*

class Theater : AppCompatActivity() {

    var meterSum=0.0
    var sum=0.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_theater)

            val sumWatcher = object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    if(!s.toString().isEmpty())
                    sum=s.toString().toDouble()
                    priceTotal.setText(" ريال "+sum*meterSum+"السعر النهائى ")
                }

                override fun afterTextChanged(s: Editable) {

                }
            }
            val meterWatcher = object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    if(!s.toString().isEmpty())
                    meterSum=s.toString().toDouble()
                    priceTotal.setText(" ريال "+sum*meterSum+" السعر النهائى  ")

                }

                override fun afterTextChanged(s: Editable) {

                }
            }
            meterPriceText.addTextChangedListener(sumWatcher)
            meterSumText.addTextChangedListener(meterWatcher)
    }

    fun finish(view: View) {
        finish()
    }
}