package com.karim.myapplication.Activites.Orders

import android.app.ActionBar
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.karim.myapplication.Model.Montag
import com.karim.myapplication.Model.PhotoGraph
import com.karim.myapplication.Model.TypesItems
import com.karim.myapplication.Model.photoData
import com.karim.myapplication.R
import kotlinx.android.synthetic.main.activity_montage_order.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.math.max
import android.graphics.Rect


class PhotoOrderActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_order)
    }

    fun finish(view: View) {
        finish()
    }
}
