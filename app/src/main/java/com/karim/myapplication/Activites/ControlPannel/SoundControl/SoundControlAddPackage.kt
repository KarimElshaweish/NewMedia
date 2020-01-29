package com.karim.myapplication.Activites.ControlPannel.SoundControl

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.karim.myapplication.Adapter.RVItemsAdapter
import com.karim.myapplication.Adapter.RvAdapter
import com.karim.myapplication.model.PhotoGraph
import com.karim.myapplication.model.TypesItems
import com.karim.myapplication.R
import com.kofigyan.stateprogressbar.StateProgressBar
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.activity_photo_grapher_add_package.*

class SoundControlAddPackage : AppCompatActivity() {
    var descriptionData = arrayOf("إسم الباقة", "سعر الباقة", "تفاصيل الباقة","نشر")

    var typecheckd=false
    var current=0
    private fun showToast(msg:String){
        Toast.makeText(baseContext,msg, Toast.LENGTH_LONG).show()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sound_control_add_package)
        rv.setHasFixedSize(true)
        rv.layoutManager= LinearLayoutManager(this)
        var listItems= ArrayList<TypesItems>()
        var adapter= RvAdapter(listItems,this)
        rv.adapter=adapter
        btnAddItem.setOnClickListener{
            var itemText=item_edit.text.toString()
            if(!itemText.equals("")){
                item_edit.setText("")
                listItems.add(TypesItems(itemText))
                adapter.notifyDataSetChanged()
            }
        }
        pbState.setStateDescriptionTypeface("font/sda.ttf")
        pbState.setStateNumberTypeface("font/sda.ttf")
        pbState.setStateDescriptionData(descriptionData)
        pbState.setDescriptionTopSpaceIncrementer(10f)
        pbState.setDescriptionLinesSpacing(5f)
        next.setOnClickListener{
            val dialog: android.app.AlertDialog? = SpotsDialog.Builder()
                .setContext(this)
                .setTheme(R.style.Custom)
                .build()
            if(typecheckd) {
                previous.setTextColor(Color.parseColor("#ffffff"))
                previous.setBackgroundDrawable(resources.getDrawable(R.drawable.border_online))
                val img =
                    getResources().getDrawable(com.karim.myapplication.R.drawable.ic_left_online)
                img.setBounds(0, 0, 60, 60)
                previous.setCompoundDrawables(img, null, null, null)
                current++
                if (current == 1) {
                    if(pk_name.text.isEmpty()) {
                        pk_name.error = getString(R.string.error_input)
                        current--
                    }
                    else {
                        pbState.setCurrentStateNumber(StateProgressBar.StateNumber.TWO)
                        viewGone(type_layout, price_layout)
                    }
                } else if (current == 2) {
                    if(price.text.isEmpty()) {
                        price.error = getString(R.string.error_input)
                        current--
                    }
                    else {
                        next.text="إنهاء"
                        next.setCompoundDrawables(null, null, null, null)
                        pbState.setCurrentStateNumber(StateProgressBar.StateNumber.THREE)
                        viewGone(price_layout, items_list)
                    }
                } else if (current == 3) {
                    if(listItems.size==0) {
                        Toast.makeText(this, getString(R.string.insert_items), Toast.LENGTH_SHORT)
                            .show()
                        current--
                    }
                        else {
                        next.text=("نشر")
                        pbState.setCurrentStateNumber(StateProgressBar.StateNumber.FOUR)
                        viewGone(adding_form, pk_form)
                        pk_title.text=(pk_name.text.toString())
                        val itemAdapter = RVItemsAdapter(this, listItems)
                        item_rv.setHasFixedSize(true)
                        item_rv.layoutManager = LinearLayoutManager(this)
                        item_rv.adapter = itemAdapter
                        price_view.text=(price.text.toString())
                    }
                } else if (next.text==("نشر")) {
                    dialog!!.show()
                    if (::filepath.isInitialized) {
                        val mrefernace = FirebaseStorage.getInstance().getReference("PKImage")
                        val uploadImage = mrefernace.putFile(filepath)
                        uploadImage.addOnFailureListener { ex ->
                            Toast.makeText(
                                baseContext,
                                ex.message,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                            .addOnSuccessListener {
                                uploadImage.continueWithTask { task ->
                                    if (!task.isSuccessful) {
                                    }
                                    mrefernace.downloadUrl
                                }.addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        val downloadUri = task.result
                                        var photoData = PhotoGraph()
                                        photoData.name = pk_name.text.toString()
                                        photoData.price = price.text.toString()
                                        photoData.items = listItems
                                        photoData.image = downloadUri.toString()
                                        FirebaseDatabase.getInstance().getReference("Sound")
                                            .child(photoData.name).setValue(photoData)
                                            .addOnCompleteListener {
                                                dialog.dismiss()
                                                showToast("تم النشر")
                                                finish()
                                            }.addOnFailureListener {
                                                showToast("حدث خطأ")
                                            }

                                    }
                                }
                            }
                    }
                    else{
                        var photoData = PhotoGraph()
                        photoData.name = pk_name.text.toString()
                        photoData.price = price.text.toString()
                        photoData.items = listItems
                        photoData.image = ""
                        FirebaseDatabase.getInstance().getReference("Sound")
                            .child(photoData.name).setValue(photoData)
                            .addOnCompleteListener {
                                dialog.dismiss()
                                showToast("تم النشر")
                                finish()
                            }.addOnFailureListener {
                                showToast("حدث خطأ")
                            }
                    }
                }
            }else{
                Toast.makeText(this,"من فضلك ادخل صورة المنتج",Toast.LENGTH_LONG).show()
            }
        }

        previous.setOnClickListener{
            current--
            if(next.text.equals("إنهاء")){

                next.setTextColor(Color.parseColor("#ffffff"))
                next.setBackgroundDrawable(resources.getDrawable(R.drawable.border_online))
                val img = getResources().getDrawable(R.drawable.ic_right_arrow_online)
                img.setBounds(0, 0, 60, 60)
                next.setCompoundDrawables(null,null,img,null)
                next.setText("التالى")
            }
            if(current==0){
                previous.setTextColor(Color.parseColor("#BDBDBD"))
                previous.setBackgroundDrawable(resources.getDrawable(R.drawable.border_offline))
                val img = getResources().getDrawable(R.drawable.ic_left_arrow_offline)
                img.setBounds(0, 0, 60, 60)
                previous.setCompoundDrawables(img,null,null,null)
                pbState.setCurrentStateNumber(StateProgressBar.StateNumber.ONE)
                viewGone(price_layout,type_layout)
            }
            if (current==1){
                previous.setTextColor(Color.parseColor("#ffffff"))
                previous.setBackgroundDrawable(resources.getDrawable(R.drawable.border_online))
                val img = getResources().getDrawable(R.drawable.ic_left_online)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(data!=null&&resultCode== Activity.RESULT_OK&&requestCode==PICK_IMAGE){
            filepath= data.data!!
            Glide.with(this).load(filepath).into(pkPicture)
            Glide.with(this).load(filepath).into(pkIamge)
        }
    }
    var PICK_IMAGE:Int=77
    lateinit var filepath: Uri
    fun chengePicture(view: View) {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(
                intent,
                "اختار صورة المنتج"
            ), PICK_IMAGE
        )
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
                view.visibility = View.GONE
                view1.visibility= View.VISIBLE
            }
        })

        view.startAnimation(animation)
    }
}

