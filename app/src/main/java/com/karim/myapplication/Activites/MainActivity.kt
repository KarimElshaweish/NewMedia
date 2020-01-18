package com.karim.myapplication.Activites

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.iid.FirebaseInstanceId
import com.shrikanthravi.customnavigationdrawer2.widget.SNavigationDrawer
import com.karim.myapplication.Fragments.*
import com.karim.myapplication.Notification.Token
import com.karim.myapplication.R
import com.karim.myapplication.Util
import com.shrikanthravi.customnavigationdrawer2.data.MenuItem
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    var back=false
    public fun finish(v:View){
        finish()
    }
    override fun onBackPressed() {
        if(back)
            finish()
        else
            back=true
    }
    var color1 = 0
    var fragment: Fragment? = null
    private fun updateToken(token:String){
        var ref= FirebaseDatabase.getInstance().reference
        var query=ref.child("Tokens")
        var token1= Token(token)
        query.child(FirebaseAuth.getInstance().currentUser!!.uid).setValue(token1)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if(FirebaseAuth.getInstance().currentUser==null){
            startActivity(Intent(this,SplashScreen::class.java))
            finish()
        }else {
            if (FirebaseAuth.getInstance().currentUser!!.uid != "tNhsqknIyefQ1CztCTgiX4XIlCf1")
                Util.empolyee = true

            if (supportActionBar != null) {
                supportActionBar!!.hide()
            }
            updateToken(FirebaseInstanceId.getInstance().token!!)

            val menuItems = arrayListOf<MenuItem>()

            if (!Util.empolyee) {
                menuItems.add(MenuItem("لوحة التحكم", R.drawable.admin_bag))
                menuItems.add(MenuItem(" الحجوزات", R.drawable.secrtary_bag))
                menuItems.add(MenuItem("الاعمال المنجزه", R.drawable.work))
                menuItems.add(MenuItem("الطلبات", R.drawable.work))
                menuItems.add(MenuItem("التقارير", R.drawable.admin_bag))
            }
            menuItems.add(MenuItem("قسم التصوير ", R.drawable.photgrapher_dag))
            menuItems.add(MenuItem("قسم التسجيل صوتى", R.drawable.music_bg))
            menuItems.add(MenuItem("قسم المسارح والشاشات", R.drawable.theater_bag))
            menuItems.add(MenuItem("قسم المونتاج", R.drawable.montag_bag))
            menuItems.add(MenuItem("السلة", R.drawable.shoppinh_bag))
            if (Util.empolyee) {
                menuItems.add(MenuItem("الاعمال المنجزه", R.drawable.work))
            }
            menuItems.add(MenuItem("تسجيل الخروج", R.drawable.admin_bag))
            navigationDrawer.setMenuItemList(menuItems)
            var fragmentClass: Class<*>
            fragmentClass = Defualt_Fregmant::class.java
            try {
                fragment = (fragmentClass.newInstance())
            } catch (e: Exception) {
                e.printStackTrace()
            }
            if (fragment != null) {
                val fragmentManager = supportFragmentManager
                fragmentManager.beginTransaction()
                    .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                    .replace(R.id.frameLayout, fragment!!).commit()
            }



            if (Util.empolyee) {
                navigationDrawer.setOnMenuItemClickListener(SNavigationDrawer.OnMenuItemClickListener { position ->
                    println("Position $position")

                    when (position) {
                        0 -> {
                            color1 = R.color.red
                            fragmentClass = PhotographerFragmetn::class.java
                        }
                        1 -> {
                            color1 = R.color.orange
                            fragmentClass = SoundFragment::class.java
                        }
                        2 -> {
                            color1 = R.color.green
                            fragmentClass = TheaterAndScreens::class.java
                        }
                        3 -> {
                            color1 = R.color.blue
                            fragmentClass = MontagFragment::class.java
                        }
                        4 -> {
                            color1 = R.color.colorAccent
                            fragmentClass = BasketFragment::class.java
                        }
                        5 -> {
                            fragmentClass = WorkDoneFragment::class.java
                        }
                        6 -> {
                            logout()
                        }
                    }

                    navigationDrawer.setDrawerListener(object : SNavigationDrawer.DrawerListener {

                        override fun onDrawerOpened() {

                        }

                        override fun onDrawerOpening() {

                        }

                        override fun onDrawerClosing() {
                            println("Drawer closed")

                            try {
                                fragment = (fragmentClass.newInstance() as Fragment)
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }

                            if (fragment != null) {
                                val fragmentManager = supportFragmentManager
                                fragmentManager.beginTransaction().setCustomAnimations(
                                    android.R.animator.fade_in,
                                    android.R.animator.fade_out
                                ).replace(R.id.frameLayout, fragment!!).commit()

                            }
                        }

                        override fun onDrawerClosed() {

                        }

                        override fun onDrawerStateChanged(newState: Int) {
                            println("State $newState")
                        }
                    })
                })

            } else {
                navigationDrawer.setOnMenuItemClickListener(SNavigationDrawer.OnMenuItemClickListener { position ->
                    println("Position $position")
                    when (position) {
                        0 -> {
                            color1 = R.color.red
                            fragmentClass = ControlPanelFragment::class.java
                        }//لوحة التحكم
                        1 -> {
                            color1 = R.color.orange
                            fragmentClass = OrderFragment::class.java
                        }//الحجوزات
                        2 -> {
                            fragmentClass = WorkDoneFragment::class.java
                        }//المنجزات
                        3 -> {
                            fragmentClass = PastOrderFragment::class.java
                        }//الطلبات
                        4 -> {
                            fragmentClass = ReportFragment::class.java
                        }//تقارير
                        5 -> {
                            color1 = R.color.green
                            fragmentClass = PhotographerFragmetn::class.java
                        }
                        6 -> {
                            color1 = R.color.blue
                            fragmentClass = SoundFragment::class.java
                        }
                        7 -> {
                            color1 = R.color.colorAccent
                            fragmentClass = TheaterAndScreens::class.java
                        }
                        8 -> {
                            color1 = R.color.colorAccent
                            fragmentClass = MontagFragment::class.java

                        }
                        9 -> {
                            color1 = R.color.colorAccent
                            fragmentClass = BasketFragment::class.java
                        }
                        10 -> {
                            logout()
                        }
                    }
                    navigationDrawer.setDrawerListener(object : SNavigationDrawer.DrawerListener {

                        override fun onDrawerOpened() {

                        }

                        override fun onDrawerOpening() {

                        }

                        override fun onDrawerClosing() {
                            println("Drawer closed")

                            try {
                                fragment = (fragmentClass.newInstance() as Fragment)
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }

                            if (fragment != null) {
                                val fragmentManager = supportFragmentManager
                                fragmentManager.beginTransaction().setCustomAnimations(
                                    android.R.animator.fade_in,
                                    android.R.animator.fade_out
                                ).replace(R.id.frameLayout, fragment!!).commit()

                            }
                        }

                        override fun onDrawerClosed() {

                        }

                        override fun onDrawerStateChanged(newState: Int) {
                            println("State $newState")
                        }
                    })
                })

            }

        }
    }
    fun logout(){
        FirebaseAuth.getInstance().signOut()
        startActivity(Intent(this,FirebaseAutActivity::class.java))
    }
}
