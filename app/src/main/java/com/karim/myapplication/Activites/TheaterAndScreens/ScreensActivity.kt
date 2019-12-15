package com.karim.myapplication.Activites.TheaterAndScreens

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import com.karim.myapplication.Model.ScreenType
import com.karim.myapplication.R
import com.karim.myapplication.Util
import kotlinx.android.synthetic.main.activity_theater.*

class ScreensActivity : AppCompatActivity() {
    var meterSum=0.0
    var sum=0.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_screens)
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
    fun btnClicked(view: View) {
        when(view.id){
            R.id.add_fab->{
                Util.listScreen.add(ScreenType((sum*meterSum).toString(),meterSum.toString(),"شاشة"))
                Toast.makeText(this,"تم الاضافة", Toast.LENGTH_SHORT).show()
                meterPriceText.setText("0")
                meterSumText.setText("0")
            }
        }
    }
}
