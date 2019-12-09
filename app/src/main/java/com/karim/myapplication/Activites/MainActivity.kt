package com.karim.myapplication.Activites

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.shrikanthravi.customnavigationdrawer2.widget.SNavigationDrawer
import com.karim.myapplication.Fragments.*
import com.karim.myapplication.R
import com.karim.myapplication.Util
import com.shrikanthravi.customnavigationdrawer2.data.MenuItem
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    var color1 = 0
    var fragment: Fragment? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }

        val menuItems = arrayListOf<MenuItem>()

        if(!Util.empolyee) {
            menuItems.add(MenuItem("لوحة التحكم", R.drawable.admin_bag))
            menuItems.add(MenuItem(" الحجوزات", R.drawable.secrtary_bag))
        }
        menuItems.add(MenuItem("قسم التصوير ", R.drawable.photgrapher_dag))
        menuItems.add(MenuItem("قسم التسجيل صوتى", R.drawable.music_bg))
        menuItems.add(MenuItem("قسم المسارح والشاشات", R.drawable.theater_bag))
        menuItems.add(MenuItem("قسم المونتاج", R.drawable.montag_bag))
        menuItems.add(MenuItem("السلة", R.drawable.shoppinh_bag))


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



        if(Util.empolyee) {
            navigationDrawer.setOnMenuItemClickListener(SNavigationDrawer.OnMenuItemClickListener { position ->
                println("Position $position")

                when (position) {

                    0 -> {
                        color1 = R.color.red
                        fragmentClass = PhotographerFragmetn::class.java
                    }
                    1 -> {
                        color1 = R.color.orange
                        fragmentClass = PhotographerFragmetn::class.java
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
                        fragmentClass = MontagFragment::class.java
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

        }else{
            navigationDrawer.setOnMenuItemClickListener(SNavigationDrawer.OnMenuItemClickListener { position ->
                println("Position $position")

                when (position) {

                    0 -> {
                        color1 = R.color.red
                        fragmentClass = ControlPanelFragment::class.java
                    }
                    1 -> {
                        color1 = R.color.orange
                        fragmentClass = FeedFragment::class.java
                    }
                    2 -> {
                        color1 = R.color.green
                        fragmentClass = PhotographerFragmetn::class.java
                    }
                    3 -> {
                        color1 = R.color.blue
                        fragmentClass = PhotographerFragmetn::class.java
                    }
                    4 -> {
                        color1 = R.color.colorAccent
                        fragmentClass = TheaterAndScreens::class.java
                    }
                    5 -> {
                        color1 = R.color.colorAccent
                        fragmentClass = MontagFragment::class.java

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
