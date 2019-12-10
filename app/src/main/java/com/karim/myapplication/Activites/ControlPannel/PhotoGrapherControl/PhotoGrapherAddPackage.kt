package com.karim.myapplication.Activites.ControlPannel.PhotoGrapherControl

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.FirebaseDatabase
import com.karim.myapplication.Adapter.RVItemsAdapter
import com.karim.myapplication.Adapter.RvAdapter
import com.karim.myapplication.Model.PhotoGraph
import com.karim.myapplication.Model.TypesItems
import com.karim.myapplication.R
import com.kofigyan.stateprogressbar.StateProgressBar
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.activity_photo_grapher_add_package.*


class PhotoGrapherAddPackage : AppCompatActivity() {

    var descriptionData = arrayOf("إسم الباقة", "سعر الباقة", "تفاصيل الباقة","نشر")

    var check=false
    var typecheckd=false
    var current=0
    var spinnerPosition:Int=-1
    private fun showToast(msg:String){
        Toast.makeText(baseContext,msg,Toast.LENGTH_LONG).show()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_grapher_add_package)

        rv.setHasFixedSize(true)
        rv.layoutManager=LinearLayoutManager(this)
        var listItems= ArrayList<TypesItems>()
        var adapter=RvAdapter(listItems,this)
        rv.adapter=adapter

        btnAddItem.setOnClickListener{
            var itemText=item_edit.text.toString()
            if(!itemText.equals("")){
                item_edit.setText("")
                listItems.add(TypesItems(itemText))
                adapter.notifyDataSetChanged()
//                adapter= RvAdapter(listItems,this)
//                rv.adapter=adapter
            }
        }
        pbState.setStateDescriptionTypeface("font/merssi_semibold.ttf")
        pbState.setStateNumberTypeface("font/merssi_semibold.ttf")
        pbState.setStateDescriptionData(descriptionData)
        pbState.setDescriptionTopSpaceIncrementer(10f);
        pbState.setDescriptionLinesSpacing(5f)
//        val list= listOf(
//            TypesPackages("باقة ماسية",R.drawable.diamond_package),
//            TypesPackages("باقة ذهبية",R.drawable.gold_package),
//            TypesPackages("باقة فضية",R.drawable.silver_pacakge),
//            TypesPackages("باقة برونزية",R.drawable.bronze_pakcage)
//        )

//        package_type_spinner.adapter=SpinnerCustomeAdapter(
//            this,
//            list
////        )
//        package_type_spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
//            override fun onItemSelected(parent:AdapterView<*>, view: View, position: Int, id: Long){
//                // Display the selected item text on text view
//                if(check) {
//                    spinnerPosition=position
//                    typecheckd=true
//                    next.setTextColor(Color.parseColor("#ffffff"))
//                    next.setBackgroundDrawable(resources.getDrawable(R.drawable.border_online))
//                    val img = getResources().getDrawable(com.karim.myapplication.R.drawable.ic_right_arrow_online)
//                    img.setBounds(0, 0, 60, 60)
//                    next.setCompoundDrawables(null,null,img,null)
//                  //  pbState.setCurrentStateNumber(StateProgressBar.StateNumber.TWO)
//                //    pbState.enableAnimationToCurrentState(true)
//                }else{
//                    check=true
//                }
//            }
//
//            override fun onNothingSelected(parent: AdapterView<*>){
//                // Another interface callback
//            }
//        }
        next.setOnClickListener{
            val dialog: android.app.AlertDialog? = SpotsDialog.Builder()
                .setContext(this)
                .setTheme(R.style.Custom)
                .build()
            if(typecheckd){
                previous.setTextColor(Color.parseColor("#ffffff"))
                previous.setBackgroundDrawable(resources.getDrawable(R.drawable.border_online))
                val img = getResources().getDrawable(com.karim.myapplication.R.drawable.ic_left_online)
                img.setBounds(0, 0, 60, 60)
                previous.setCompoundDrawables(img,null,null,null)
                current++
                if(current==1){
                    pbState.setCurrentStateNumber(StateProgressBar.StateNumber.TWO)
                    viewGone(type_layout,price_layout)
                }
               else if(current==2) {
                    next.setText("إنهاء")
                    next.setCompoundDrawables(null,null,null,null)
                    pbState.setCurrentStateNumber(StateProgressBar.StateNumber.THREE)
                    viewGone(price_layout,items_list)
                }
               else if(current==3){
                    next.setText("نشر")
                    pbState.setCurrentStateNumber(StateProgressBar.StateNumber.FOUR)
                    viewGone(adding_form,pk_form)
                  //  var pk=list.get(spinnerPosition)
                  //  img_type.setImageDrawable(resources.getDrawable(pk.image))
                    pk_title.setText(pk_name.text.toString())
                    var itemAdapter=RVItemsAdapter(this,listItems)
                    item_rv.setHasFixedSize(true)
                    item_rv.layoutManager=LinearLayoutManager(this)
                    item_rv.adapter=itemAdapter
                    price_view.setText(price.text.toString())
                }
               else if(next.text.equals("نشر")){
                    dialog!!.show()
                    var photoData= PhotoGraph()
                    photoData.name=pk_name.text.toString()
                    photoData.price=price.text.toString()
                    photoData.items=listItems
                    FirebaseDatabase.getInstance().getReference("photoGraph")
                        .child(photoData.name).setValue(photoData).addOnCompleteListener{
                            dialog.dismiss()
                            showToast("تم النشر")
                            finish()
                    }.addOnFailureListener{
                        showToast("حدث خطأ")
                    }
                }
            }
        }
        previous.setOnClickListener{
            current--
            if(next.text.equals("إنهاء")){

                next.setTextColor(Color.parseColor("#ffffff"))
                next.setBackgroundDrawable(resources.getDrawable(R.drawable.border_online))
                val img = getResources().getDrawable(com.karim.myapplication.R.drawable.ic_right_arrow_online)
                img.setBounds(0, 0, 60, 60)
                next.setCompoundDrawables(null,null,img,null)
                next.setText("التالى")
            }
            if(current==0){
                previous.setTextColor(Color.parseColor("#BDBDBD"))
                previous.setBackgroundDrawable(resources.getDrawable(R.drawable.border_offline))
                val img = getResources().getDrawable(com.karim.myapplication.R.drawable.ic_left_arrow_offline)
                img.setBounds(0, 0, 60, 60)
                previous.setCompoundDrawables(img,null,null,null)
                pbState.setCurrentStateNumber(StateProgressBar.StateNumber.ONE)
                viewGone(price_layout,type_layout)
            }
            if (current==1){
                previous.setTextColor(Color.parseColor("#ffffff"))
                previous.setBackgroundDrawable(resources.getDrawable(R.drawable.border_online))
                val img = getResources().getDrawable(com.karim.myapplication.R.drawable.ic_left_online)
                img.setBounds(0, 0, 60, 60)
                previous.setCompoundDrawables(img,null,null,null)
                pbState.setCurrentStateNumber(StateProgressBar.StateNumber.TWO)
                viewGone(items_list,price_layout)
            }
            if(next.text.equals("نشر")){
                next.setText("إنهاء")
                viewGone(pk_form,adding_form)
                pbState.setCurrentStateNumber(StateProgressBar.StateNumber.THREE)
            }

        }
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if(!s.toString().isEmpty()){
                    next.setTextColor(Color.parseColor("#ffffff"))
                    next.setBackgroundDrawable(resources.getDrawable(R.drawable.border_online))
                    val img = getResources().getDrawable(com.karim.myapplication.R.drawable.ic_right_arrow_online)
                    img.setBounds(0, 0, 60, 60)
                    next.setCompoundDrawables(null,null,img,null)
                    typecheckd=true
                }else{
                    typecheckd=false
                    next.setTextColor(Color.parseColor("#BDBDBD"))
                    next.setBackgroundDrawable(resources.getDrawable(R.drawable.border_offline))
                    val img = getResources().getDrawable(R.drawable.ic_right_arrow)
                    img.setBounds(0, 0, 60, 60)
                    next.setCompoundDrawables(null,null,img,null)

                }
            }

            override fun afterTextChanged(s: Editable) {

            }
        }
        pk_name.addTextChangedListener(textWatcher)

    }

    fun finish(view: View) {
        finish()
    }
    fun viewGone(view:View,view1:View){
        val animation = AnimationUtils.loadAnimation(this, R.anim.translate_down)
        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}

            override fun onAnimationRepeat(animation: Animation) {}

            override fun onAnimationEnd(animation: Animation) {
                view.visibility = GONE
                view1.visibility= VISIBLE
            }
        })

        view.startAnimation(animation)
    }
}
