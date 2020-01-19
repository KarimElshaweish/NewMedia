package com.karim.myapplication.Fragments

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.*
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.iid.FirebaseInstanceId
import com.karim.myapplication.Adapter.BasketRVAdapter
import com.karim.myapplication.Adapter.RVAdapterPkItems
import com.karim.myapplication.Adapter.ScreenBasketRVAdapter
import com.karim.myapplication.Adapter.TheaterBasketAdapter
import com.karim.myapplication.Interfaces.OnBasketDataLoadLisenter
import com.karim.myapplication.Notification.*
import com.karim.myapplication.R
import com.karim.myapplication.Util
import com.karim.myapplication.ViewModel.BasketViewModel
import com.karim.myapplication.databinding.FragmentBasketBinding
import com.karim.myapplication.model.*
import com.kofigyan.stateprogressbar.StateProgressBar
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.fragment_basket.*
import kotlinx.android.synthetic.main.sound_report.report
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class BasketFragment : Fragment(),
    OnBasketDataLoadLisenter {

    lateinit var printView1: ScrollView
    lateinit var rvitems: RecyclerView
    lateinit var rvitems1: RecyclerView
    lateinit var noSoundText: TextView
    lateinit var noPhotoText: TextView
    lateinit var noTheaterText: TextView
    lateinit var soundMoneyLayout: LinearLayout
    lateinit var photoMoneyLayout: LinearLayout
    lateinit var theaterMoneyLayout: LinearLayout

    lateinit var soundLayout: LinearLayout
    lateinit var workDate: TextView
    var myCalendar=Calendar.getInstance()
    var svPhoto: ScrollView? = null
    var basketViewModel = BasketViewModel()
    var dialog: AlertDialog? = null
    var pd: photoData? = null
    var screenData:ScreenUploadData?=null
    var theaterData:TheaterUploadData?=null
    private fun createTextLabel(text:String): TextView {
        val tableRowparam = TableRow.LayoutParams()
        tableRowparam.weight=1f
        val textView = TextView(context)
        textView.width=resources.getDimension(R.dimen.textViewWidth).toInt()
        textView.setBackgroundDrawable(resources.getDrawable(R.drawable.cell_border))
        textView.layoutParams=tableRowparam
        textView.text = text
        textView.gravity= Gravity.CENTER
        textView.setTextColor(Color.BLACK)
        textView.setPadding(2,2,2,2)
        return textView
    }


    private fun createTableRow(obj: Any) {
        var tableRow = TableRow(context!!)
        if (obj is TheaterData) {
            var theater = obj
            tableRow.addView(createTextLabel(theater.price))
            tableRow.addView(createTextLabel(theater.totalMeter))
            binding!!.theaterTable.addView(tableRow)
        } else {
            var screen=obj as ScreenType
            tableRow.addView(createTextLabel(screen.price))
            tableRow.addView(createTextLabel(screen.totalMeter))
            binding!!.screenTable.addView(tableRow)
        }
    }
    var state = 0
    var api_services:APIServices?=null
    fun nextfun() {
        when (state) {
            0 -> {
                if(Util.soundList.size!=0){
                    if(binding!!.soundCheckBox.isChecked){
                        binding!!.soundMoneyGet.setText(soundTotalPrice().toString())
                        binding!!.soundMoneyGet.setClickable(false)
                        binding!!.soundMoneyRest.isClickable=false
                        binding!!.soundMoneyRest.setText("0")
                    }
                    if(binding!!.soundMoneyGet.text.isEmpty())
                        binding!!.soundMoneyGet.error= getString(R.string.please_enter_moneyGet)
                    else if(binding!!.soundMoneyRest.text.isEmpty())
                        binding!!.soundMoneyRest.error=getString(R.string.please_enter_moneyRest)
                    else{
                        binding!!.soundMoneyLayout.visibility=View.GONE
                        binding!!.pbState.setCurrentStateNumber(StateProgressBar.StateNumber.TWO)
                        state++
                        soundLayout.visibility = View.GONE
                        makeOnline(prevouis!!, true)
                        binding!!.photoLayout.visibility = View.VISIBLE
                        if(Util.list.size!=0)
                            seePhoto()
                        else
                            noPhoto()
                    }
                }else {
                    binding!!.pbState.setCurrentStateNumber(StateProgressBar.StateNumber.TWO)
                    state++
                    soundLayout.visibility = View.GONE
                    makeOnline(prevouis!!, true)
                    binding!!.photoLayout.visibility = View.VISIBLE
                    if(Util.list.size!=0)
                        seePhoto()
                    else
                        noPhoto()
                }

            }
            1 -> {
                if(Util.list.size!=0){
                    if(binding!!.photoMoneyGet.text.isEmpty())
                        binding!!.photoMoneyGet.error=getString(R.string.please_enter_moneyGet)
                    else if(binding!!.photoMoneyRest.text.isEmpty())
                        binding!!.photoMoneyRest.error=getString(R.string.please_enter_moneyRest)
                    else{
                        noPhoto()
                        if (Util.theaterList.size != 0)
                            seeTheater()
                        else
                            noTheater()
                        binding!!.pbState.setCurrentStateNumber(StateProgressBar.StateNumber.THREE)
                        state++
                        binding!!.photoLayout.visibility = View.GONE
                        binding!!.theaterLayout.visibility = View.VISIBLE
                    }
                }else{
                    noPhoto()
                    binding!!.theaterLayout.visibility = View.VISIBLE
                    if (Util.theaterList.size != 0) {
                        seeTheater()
                    }
                    else {
                        noTheater()
                    }
                    binding!!.pbState.setCurrentStateNumber(StateProgressBar.StateNumber.THREE)
                    state++
                    binding!!.photoLayout.visibility = View.GONE
                }
            }
            2->{

                if(Util.theaterList.size!=0){
                    if(binding!!.theaterMoneyGet.text.isEmpty())
                        binding!!.theaterMoneyGet.error=getString(R.string.please_enter_moneyGet)
                    else if(binding!!.theaterMoneyRest.text.isEmpty())
                        binding!!.theaterMoneyRest.error=getString(R.string.please_enter_moneyRest)
                    else{
                        noTheater()
                        if(Util.listScreen.size==0){
                            binding!!.screenLayout.visibility = View.VISIBLE
                            binding!!.noscreenText.visibility=View.VISIBLE
                            binding!!.screenMoneyLayout.visibility=View.GONE
                        }else{
                            binding!!.screenLayout.visibility = View.VISIBLE
                            binding!!.screenMoneyLayout.visibility=View.VISIBLE
                            binding!!.noscreenText.visibility=View.GONE
                        }
                        binding!!.pbState.setCurrentStateNumber(StateProgressBar.StateNumber.FOUR)
                        state++
                        binding!!.screenLayout.visibility = View.VISIBLE
                        binding!!.theaterLayout.visibility = View.GONE
                    }
                }else {
                    noTheater()
                    if(Util.listScreen.size==0){
                        binding!!.screenLayout.visibility = View.VISIBLE
                        binding!!.noscreenText.visibility=View.VISIBLE
                        binding!!.screenMoneyLayout.visibility=View.GONE
                    }else{
                        binding!!.screenLayout.visibility = View.VISIBLE
                        binding!!.screenMoneyLayout.visibility=View.VISIBLE
                        binding!!.noscreenText.visibility=View.GONE
                    }
                    binding!!.pbState.setCurrentStateNumber(StateProgressBar.StateNumber.FOUR)
                    state++
                    binding!!.theaterLayout.visibility = View.GONE
                }
            }
            3 -> {

                if(Util.listScreen.size!=0){
                    if(binding!!.screenMoneyGet.text.isEmpty())
                        binding!!.screenMoneyGet.error=getString(R.string.please_enter_moneyGet)
                    else if(binding!!.screenMoneyRest.text.isEmpty())
                        binding!!.screenMoneyRest.error=getString(R.string.please_enter_moneyRest)
                    else{
                        state++
                        binding!!.pbState.setCurrentStateNumber(StateProgressBar.StateNumber.FIVE)
                        binding!!.screenLayout.visibility = View.GONE
                        binding!!.screenMoneyLayout.visibility=View.GONE
                        binding!!.informationLayout.visibility = View.VISIBLE
                        binding!!.next.text = getString(R.string.printPdf)
                        binding!!.next.setCompoundDrawables(null, null, null, null)
                    }
                }else {
                    binding!!.pbState.setCurrentStateNumber(StateProgressBar.StateNumber.FIVE)
                    state++
                    binding!!.screenMoneyLayout.visibility=View.GONE
                    binding!!.screenLayout.visibility = View.GONE
                    binding!!.informationLayout.visibility = View.VISIBLE
                    binding!!.next.text = getString(R.string.printPdf)
                    binding!!.next.setCompoundDrawables(null, null, null, null)
                }
            }
            4->{
                if(!validation(binding!!.clientName,binding!!.moneyGetWay,
                        binding!!.workLocation,binding!!.phoneNumber)){
                    Toast.makeText(context,getString(R.string.please_enter_all_data),Toast.LENGTH_SHORT).show()
                }else{
                    if(Util.list.size==0&&Util.soundList.size==0&&Util.theaterList.size==0&&Util.listScreen.size==0)
                        Toast.makeText(context,getString(R.string.noPk_added),Toast.LENGTH_SHORT).show()
                    else{
                        state++
                        binding!!.next.text=getString(R.string.share)
                        var basketData=BasketData(clientName.text.toString(),
                            moneyGetWay.text.toString(),
                            phoneNumber.text.toString(),
                            workLocation.text.toString(),
                            binding!!.workDate.text.toString(),binding!!.soundMoneyGet.text.toString(),
                            binding!!.soundMoneyRest.text.toString(),
                            binding!!.photoMoneyGet.text.toString(),
                            binding!!.photoMoneyRest.text.toString(),
                            binding!!.theaterMoneyGet.text.toString(),binding!!.theaterMoneyRest.text.toString(),
                            binding!!.screenMoneyGet.text.toString(),binding!!.screenMoneyRest.text.toString(),binding!!.workName.text.toString())
                        binding!!.data=basketData
                        if(Util.soundList.size!=0){
                            binding!!.rvitems.setHasFixedSize(true)
                            binding!!.rvitems.layoutManager=LinearLayoutManager(context)
                            var listTypes= mutableListOf<TypesItems>()
                            var totPrice=0
                            for(item in Util.soundList){
                                listTypes.addAll(item.items)
                                totPrice+=item.price.toInt()
                            }
                            var itemAdapter=  RVAdapterPkItems(listTypes, context!!)
                            binding!!.rvitems.adapter=itemAdapter
                            binding!!.bsketFrame.visibility = View.GONE
                            binding!!.fullReportOption.visibility = View.VISIBLE
                            binding!!.soundReportView.visibility=View.VISIBLE
                        }
                        if(Util.list.size!=0){
                            var listTypes1= mutableListOf<TypesItems>()
                            var totPrice1=0
                            for(item in Util.list){
                                listTypes1.addAll(item.items)
                                totPrice1+=item.price.toInt()
                            }
                            binding!!.rvitems1.setHasFixedSize(true)
                            binding!!.rvitems1.layoutManager=LinearLayoutManager(context)
                            var itemAdapter1=  RVAdapterPkItems(listTypes1, context!!)
                            binding!!.rvitems1.adapter=itemAdapter1
                            binding!!.bsketFrame.visibility = View.GONE
                            binding!!.fullReportOption.visibility = View.VISIBLE
                            binding!!.photoReportView.visibility=View.VISIBLE
                        }
                        if(Util.theaterList.size!=0) {
                            binding!!.theaterReportView.visibility = View.VISIBLE
                            binding!!.bsketFrame.visibility = View.GONE
                            for (item in Util.theaterList) {
                                createTableRow(item)
                            }
                        }
                        if(Util.listScreen.size!=0){
                            binding!!.screenReportView.visibility=View.VISIBLE
                            binding!!.bsketFrame.visibility = View.GONE
                            for(item in Util.listScreen){
                                createTableRow(item)
                            }
                        }


                    }
                }
            }
            5->{
                api_services=
                    Client.getClient("https://fcm.googleapis.com/").create(APIServices::class.java)
                sentNotification("tNhsqknIyefQ1CztCTgiX4XIlCf1",binding!!.clientName,binding!!.workDate)
                pd = photoData(
                    emptyList(),
                    getTextString(binding!!.clientName),
                    getTextString(binding!!.photoMoneyGet),
                    getTextString(binding!!.phoneNumber),
                    getTextString(binding!!.workLocation),
                    getTextString(binding!!.workDate),"","","",binding!!.workName.text.toString()
                )
                dialog!!.show()
                when {
                    Util.soundList.size!=0 -> {
                        pd?.items=Util.soundList
                        pd?.moneyRest=binding!!.soundMoneyRest.text.toString()
                        pd?.moneyHave=binding!!.soundMoneyGet.text.toString()
                        basketViewModel.uploadMusice(pd!!)
                    }
                    Util.list.size!=0 -> {
                        pd?.items=Util.list
                        pd?.moneyRest=binding!!.photoMoneyRest.text.toString()
                        pd?.moneyHave=binding!!.photoMoneyGet.text.toString()
                        basketViewModel.uploadPhotoData(pd!!)
                    }
                    Util.theaterList.size!=0 -> {
                        theaterData=TheaterUploadData(Util.theaterList,pd!!.clientName,
                            pd!!.moneyGet,pd!!.phoneNumber,pd!!.location,pd!!.date,
                            binding!!.theaterMoneyRest.text.toString(),
                            binding!!.theaterMoneyGet.text.toString(),"",binding!!.workName.text.toString())
                        basketViewModel.uploadTheater(theaterData!!)
                    }
                    else -> {
                        screenData=ScreenUploadData(Util.listScreen,pd!!.clientName,
                            pd!!.moneyGet,pd!!.phoneNumber,pd!!.location,pd!!.date,
                            binding!!.screenMoneyRest.text.toString(),
                            binding!!.screenMoneyGet.text.toString(),"",binding!!.workName.text.toString())
                        basketViewModel.uploadScreen(screenData!!)
                    }
                }
            }
            6->share()
        }
    }

    private fun sentNotification(reciver: String, clientName: EditText?, workDate: EditText?) {
        val tokens = FirebaseDatabase.getInstance().getReference("Tokens")
        val query = tokens.orderByKey().equalTo(reciver)
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (dt in dataSnapshot.children) {
                    val token = dt.value as HashMap<*, *>
                    var data=Data(FirebaseAuth.getInstance().currentUser!!.uid,
                        clientName!!.text.toString(),workDate!!.text.toString())
                    val sender = Sender(data, token["token"].toString())
                    api_services!!.sendNotificaiton(sender).enqueue(object : Callback<MyResponse> {
                            override fun onResponse(
                                call: Call<MyResponse>,
                                response: Response<MyResponse>
                            ) {
                                if (response.code() == 200) {
                                }
                            }

                            override fun onFailure(
                                call: Call<MyResponse>,
                                t: Throwable
                            ) {
                                println(t.message)
                            }
                        })
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

    }

    fun prev() {
        when (state) {
            5->{
                state--
                binding!!.bsketFrame.visibility=View.VISIBLE
                binding!!.fullReportOption.visibility=View.GONE
            }
            4->{
                noTheater()
                binding!!.pbState.setCurrentStateNumber(StateProgressBar.StateNumber.FOUR)
                state--
                binding!!.informationLayout.visibility = View.GONE
                binding!!.screenLayout.visibility = View.VISIBLE
                binding!!.next.text = "التالى"
                makeOnline(binding!!.next, false)
            }
            3 -> {
                if(Util.theaterList.size!=0)
                seeTheater()
                noPhoto()
                binding!!.pbState.setCurrentStateNumber(StateProgressBar.StateNumber.THREE)
                state--
                binding!!.screenLayout.visibility = View.GONE
                binding!!.theaterLayout.visibility = View.VISIBLE
            }
            2 -> {
                if(Util.list.size!=0)
                seePhoto()
                noSound()
                binding!!.pbState.setCurrentStateNumber(StateProgressBar.StateNumber.TWO)
                state--
                binding!!.theaterLayout.visibility = View.GONE
                binding!!.photoLayout.visibility = View.VISIBLE
            }
            1 -> {
                if(Util.soundList.size!=0)
                seeSound()
                binding!!.previous.setTextColor(Color.parseColor("#BDBDBD"))
                binding!!.previous.setBackgroundDrawable(resources.getDrawable(R.drawable.border_offline))
                val img = getResources().getDrawable(R.drawable.ic_left_arrow_offline)
                img.setBounds(0, 0, 60, 60)
                binding!!.previous.setCompoundDrawables(img,null,null,null)
                binding!!.pbState.setCurrentStateNumber(StateProgressBar.StateNumber.ONE)
                state--
                binding!!.photoLayout.visibility = View.GONE
                binding!!.soundLayout.visibility = View.VISIBLE
            }
        }
    }
    fun updateDate( date:String){
        workDate.text=date
    }
    var descriptionData = arrayOf(" صوتيه", " تصوير", "مسارح","شاشات","العميل")

    fun makeOnline(btn:TextView,prev:Boolean){
        btn.setTextColor(Color.parseColor("#ffffff"))
        btn.setBackgroundDrawable(resources.getDrawable(R.drawable.border_online))
        if(prev){
            val img =
                getResources().getDrawable(R.drawable.ic_left_online)
            img.setBounds(0, 0, 60, 60)
            btn.setCompoundDrawables(img, null, null, null)
        }else{
            val img =
                getResources().getDrawable(R.drawable.ic_right_arrow_online)
            img.setBounds(0, 0, 60, 60)
            btn.setCompoundDrawables(img, null, null, null)
        }
    }
    var prevouis:TextView?=null
    var binding:FragmentBasketBinding?=null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        __init__ref()
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_basket, container, false)
        binding!!.photoCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                binding!!.photoMoneyGet.setText(photoTotalPrice().toString())
                binding!!.photoMoneyRest.setText("0")
                binding!!.photoMoneyGet.isEnabled=false
                binding!!.photoMoneyRest.isEnabled=false
            }else{
                binding!!.photoMoneyGet.isEnabled=true
                binding!!.photoMoneyRest.isEnabled=true
            }
        }
        binding!!.screenCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                binding!!.screenMoneyGet.setText(screenTotalPrice().toString())
                binding!!.screenMoneyGet.isEnabled=false
                binding!!.screenMoneyRest.setText("0")
                binding!!.screenMoneyRest.isEnabled=false
            }else{
                binding!!.screenMoneyGet.isEnabled=true
                binding!!.screenMoneyRest.isEnabled=true
            }
        }
        binding!!.soundCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                binding!!.soundMoneyGet.isEnabled=false
                binding!!.soundMoneyRest.isEnabled=false
                binding!!.soundMoneyGet.setText(soundTotalPrice().toString())
                binding!!.soundMoneyRest.setText("0")
            }else{
                binding!!.soundMoneyGet.isEnabled=true
                binding!!.soundMoneyRest.isEnabled=true
            }
        }
        binding!!.theaterCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                binding!!.theaterMoneyGet.setText(theaterTotalPrice().toString())
                binding!!.theaterMoneyGet.isEnabled=false
                binding!!.theaterMoneyRest.setText("0")
                binding!!.theaterMoneyRest.isEnabled=false
            }else{
                binding!!.theaterMoneyGet.isEnabled=true
                binding!!.theaterMoneyRest.isEnabled=true
            }
        }

        if(Util.soundList.size>0)
            binding!!.soundTotalPrice.text=soundTotalPrice().toString()
        if(Util.list.size>0)
            binding!!.photoTotalPrice.text=photoTotalPrice().toString()
        if(Util.theaterList.size>0)
            binding!!.theaterTotalPrice.text=theaterTotalPrice().toString()
        if(Util.listScreen.size>0)
            binding!!.screenTotalPrice.text=screenTotalPrice().toString()

        var view=binding!!.root
        var locale=Locale("ar")
        Locale.setDefault(locale)
        var config =
            context!!.getResources().getConfiguration()
        config.setLocale(locale)
        context!!.createConfigurationContext(config)
        prevouis=binding!!.previous
        binding!!.next.setOnClickListener{
            nextfun()
        }
        binding!!.previous.setOnClickListener{
            prev()
        }
        var pbState=view.findViewById<StateProgressBar>(R.id.pbState)
        soundLayout=view.findViewById<LinearLayout>(R.id.soundLayout)
        pbState.setStateDescriptionTypeface("font/merssi_semibold.ttf")
        pbState.setStateNumberTypeface("font/merssi_semibold.ttf")
        pbState.setStateDescriptionData(descriptionData)
        pbState.setDescriptionTopSpaceIncrementer(10f);
        pbState.setDescriptionLinesSpacing(5f)
       dialog= SpotsDialog.Builder()
            .setContext(context!!)
            .setTheme(R.style.upload)
            .build()
        noTheaterText=view.findViewById<TextView>(R.id.noTheaterText)
        basketViewModel=ViewModelProviders.of(this).get(BasketViewModel::class.java)
        basketViewModel.init(this)
        workDate=view.findViewById(R.id.workDate)
        var dateSetup=DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                myCalendar.set(Calendar.YEAR,year)
            myCalendar.set(Calendar.MONTH,month)
            myCalendar.set(Calendar.DAY_OF_WEEK,dayOfMonth)
            var date =  Date(year, month, dayOfMonth-1)
            var locale=Locale("ar")
            Locale.setDefault(locale)
            var config =
                context!!.getResources().getConfiguration()
            config.setLocale(locale)
            context!!.createConfigurationContext(config)
            var  sdf = SimpleDateFormat("EEEE",locale)
            var d=sdf.format(date)
            updateDate("$d-${dayOfMonth}/${month+1}/${year}")
        }

        workDate.setOnClickListener{
           DatePickerDialog(context!!,R.style.DialogTheme
                ,dateSetup,myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_WEEK)).show()

        }

       theaterMoneyLayout=view.findViewById<LinearLayout>(R.id.theaterMoneyLayout)
        photoMoneyLayout=view.findViewById(R.id.photoMoneyLayout)
        soundMoneyLayout=view.findViewById(R.id.soundMoneyLayout)
        noSoundText=view.findViewById(R.id.noSoundText)
        noPhotoText=view.findViewById(R.id.noPhotoText)
        rvitems=view.findViewById(R.id.rvitems)
        rvitems1=view.findViewById(R.id.rvitems1)
        var photoRv=view.findViewById<RecyclerView>(R.id.photo_rv)
        var soundRv=view.findViewById<RecyclerView>(R.id.sound_rv)
        soundRv.setHasFixedSize(true)
        soundRv.layoutManager=LinearLayoutManager(context)
        var photoAdapter= BasketRVAdapter(context!!, Util.list)
        photoRv.adapter=photoAdapter
        photoRv.setHasFixedSize(true)
        photoRv.layoutManager=LinearLayoutManager(context)
        var soundAdapter= BasketRVAdapter(context!!, Util.soundList)
        soundRv.adapter=soundAdapter

        var theaterRv=view.findViewById<RecyclerView>(R.id.theater_rv)
        theaterRv.setHasFixedSize(true)
        theaterRv.layoutManager=LinearLayoutManager(context)

        var theaterAdapter=TheaterBasketAdapter(context!!, Util.theaterList)
        theaterRv.adapter=theaterAdapter

        var screen_rv=view.findViewById<RecyclerView>(R.id.screen_rv)
        screen_rv.setHasFixedSize(true)
        screen_rv.layoutManager=LinearLayoutManager(context)

        var screenAdapter=ScreenBasketRVAdapter(context!!,Util.listScreen)
        screen_rv.adapter=screenAdapter

        svPhoto=view.findViewById(R.id.photoReportView)
        printView1=view.findViewById(R.id.soundReportView)
        if(Util.soundList.size==0)
            noSound()
        else
            seeSound()
        return view
    }

    private fun validation(clientName: EditText?, moneyGetWay: EditText?, workLocation: EditText?, phoneNumber: EditText?): Boolean {
        return (clientName!!.text.isNotEmpty()&&
                moneyGetWay!!.text.isNotEmpty()&&
                workLocation!!.text.isNotEmpty()&&
                phoneNumber!!.text.isNotEmpty()
                )
    }

    private fun seeSound() {
        binding!!.soundMoneyLayout.visibility = View.VISIBLE
        binding!!.noSoundText.visibility = View.GONE
    }

    private fun seeTheater() {
        binding!!.theaterMoneyLayout.visibility = View.VISIBLE
        binding!!.noTheaterText.visibility = View.GONE
    }

    private fun seePhoto() {
        binding!!.photoMoneyLayout.visibility = View.VISIBLE
        binding!!.noPhotoText.visibility = View.GONE
    }

    private fun noPhoto() {
        binding!!.photoMoneyLayout.visibility = View.GONE
        binding!!.noPhotoText.visibility = View.VISIBLE
    }

    private fun noSound() {
        binding!!.soundMoneyLayout.visibility = View.GONE
        binding!!.noSoundText.visibility = View.VISIBLE
    }

    private fun noTheater() {
        binding!!.theaterMoneyLayout.visibility = View.GONE
        binding!!.noTheaterText.visibility = View.VISIBLE
    }

    private fun __init__ref() {
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        builder.detectFileUriExposure()
    }



    fun getTextString(txtView:TextView)=txtView.text.toString()
    private fun createPdf(bitmap: Bitmap) {

        var builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        document = PdfDocument()
        if(Util.photoBolean) {
            var bitmap = bitmap
            val pageInfo = PdfDocument.PageInfo.Builder(bitmap.width, bitmap.height, 1).create()
            val page = document!!.startPage(pageInfo)
            val canvas = page.getCanvas()

            val paint = Paint()
            paint.color = Color.parseColor("#ffffff")
            canvas.drawPaint(paint)


            bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.width, bitmap.height, true)

            paint.color = Color.BLUE
            canvas.drawBitmap(bitmap, 0f, 0f, null)
            document!!.finishPage(page)
        }

        if(Util.soundList.size!=0) {
            var bitmap1 = getBitmapFromView(
                printView1,
                report.getChildAt(0).getHeight(),
                report.getWidth()
            )

            val pageInfo1 = PdfDocument.PageInfo.Builder(bitmap1.width, bitmap1.height, 1).create()
            val page1 = document!!.startPage(pageInfo1)

            val canvas1 = page1.getCanvas()


            val paint1 = Paint()
            paint1.color = Color.parseColor("#ffffff")
            canvas1.drawPaint(paint1)


            bitmap1 = Bitmap.createScaledBitmap(bitmap1, bitmap1.width, bitmap1.height, true)

            paint1.color = Color.BLUE
            canvas1.drawBitmap(bitmap1, 0f, 0f, null)
                document!!.finishPage(page1)

        }
        if(Util.theaterBolean) {
            var th = binding!!.theaterReportView
            var bitmap2 = getBitmapFromView(
                th,
                th.getChildAt(0).getHeight(),
                th.getWidth()
            )

            val pageInfo2 = PdfDocument.PageInfo.Builder(bitmap2.width, bitmap2.height, 1).create()
            val page2 = document!!.startPage(pageInfo2)

            val canvas2 = page2.getCanvas()


            val paint2 = Paint()
            paint2.color = Color.parseColor("#ffffff")
            canvas2.drawPaint(paint2)


            bitmap2 = Bitmap.createScaledBitmap(bitmap2, bitmap2.width, bitmap2.height, true)

            paint2.color = Color.BLUE
            canvas2.drawBitmap(bitmap2, 0f, 0f, null)
            document!!.finishPage(page2)
        }
        if(Util.screenBolean) {
            var bitmap3 = getBitmapFromView(
                binding!!.screenReportView,
                binding!!.screenReportView.getChildAt(0).getHeight(),
                binding!!.screenReportView.getWidth()
            )

            val pageInfo3 = PdfDocument.PageInfo.Builder(bitmap3.width, bitmap3.height, 1).create()
            val page3 = document!!.startPage(pageInfo3)

            val canvas3 = page3.getCanvas()


            val paint3 = Paint()
            paint3.color = Color.parseColor("#ffffff")
            canvas3.drawPaint(paint3)


            bitmap3 = Bitmap.createScaledBitmap(bitmap3, bitmap3.width, bitmap3.height, true)

            paint3.color = Color.BLUE
            canvas3.drawBitmap(bitmap3, 0f, 0f, null)
            document!!.finishPage(page3)

        }

        // write the document content
        // val targetPdf = context?.getExternalFilesDir(null).toString()+"/hello.pdf"
        //val targetPdf =  Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)c

        share()
        // close the document
        Util.theaterList= emptyList()
        Util.photoBolean=false
        Util.musicBolean=false
        Util.theaterBolean=false
        Util.screenBolean=false
        Util.list= emptyList()
        Util.listScreen= emptyList()
        Util.resetPhoto()
        Util.soundList= emptyList()
        binding!!.previous.visibility=View.GONE
        state++
    }

    var document:PdfDocument?=null
    private fun share() {
        filePath =
            File(context!!.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "عقد نيوميديا.pdf")
        try {
            document!!.writeTo(FileOutputStream(filePath))
        } catch (kesho: IOException) {
            kesho.printStackTrace()
            Toast.makeText(context, "Something wrong: " + kesho.toString(), Toast.LENGTH_LONG)
                .show()
        }
        val uri = Uri.fromFile(filePath)

        val share = Intent()
        share.setAction(Intent.ACTION_SEND_MULTIPLE)
        share.putParcelableArrayListExtra(Intent.EXTRA_STREAM, arrayListOf(uri))
        share.setType("text/plain")
        startActivity(share)

    }

    var filePath:File?=null
    private fun getBitmapFromView(view: View, height: Int, width: Int): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        //
        val ratioX = width / bitmap.width.toFloat()
        val ratioY = height / bitmap.height.toFloat()
        val middleX = width / 2.0f
        val middleY = height / 2.0f

        ///
        val scaleMatrix = Matrix()
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY)


        val canvas = Canvas(bitmap)
        ///
        canvas.setMatrix(scaleMatrix)
        canvas.drawBitmap(
            bitmap,
            middleX - bitmap.width / 2,
            middleY - bitmap.height / 2,
            Paint(Paint.FILTER_BITMAP_FLAG)
        )
        val bgDrawable = view.background
        if (bgDrawable != null)
            bgDrawable.draw(canvas)
        else
            canvas.drawColor(Color.WHITE)
        view.draw(canvas)
        return bitmap
    }

    override fun onPohotAddedSuccess() {
        Util.photoBolean=true
        when(Util.theaterList.size) {
            0-> {
                createPDF()
                Util.theaterList= emptyList()
            }
            else->{
                theaterData=TheaterUploadData(Util.theaterList,pd!!.clientName,
                    pd!!.moneyGet,pd!!.phoneNumber,pd!!.location,pd!!.date,
                    binding!!.theaterMoneyRest.text.toString(),
                    binding!!.theaterMoneyGet.text.toString(),"",binding!!.workName.text.toString())
                    basketViewModel.uploadTheater(theaterData!!)
            }
        }
    }

    private fun createPDF() {
        dialog!!.dismiss()
        var hight=svPhoto!!.getChildAt(0).height
        if(hight==0)
            hight=100
        var witdh=svPhoto!!.width
        if(witdh==0)
            witdh=100
        createPdf(
            getBitmapFromView(
                svPhoto!!,
               hight,
                witdh
            )
        )
    }

    override fun onPhotoAddedFailed() {
        makeToastError()
    }

    private fun makeToastError() {
        dialog!!.dismiss()
        Toast.makeText(context!!, "حدث خطأ", Toast.LENGTH_SHORT)
            .show()
    }

    override fun onMusiceAddedSuccess() {
        Util.musicBolean=true
        Util.soundList= emptyList()
        when{
            Util.list.size!=0 -> {
                pd?.items=Util.list
                pd?.moneyRest=binding!!.photoMoneyRest.text.toString()
                pd?.moneyHave=binding!!.photoMoneyGet.text.toString()
                basketViewModel.uploadPhotoData(pd!!)
            }
            Util.theaterList.size!=0 -> {
                theaterData=TheaterUploadData(Util.theaterList,pd!!.clientName,
                    pd!!.moneyGet,pd!!.phoneNumber,pd!!.location,pd!!.date,
                    binding!!.theaterMoneyRest.text.toString(),
                    binding!!.theaterMoneyGet.text.toString(),"",binding!!.workName.text.toString())
                basketViewModel.uploadTheater(theaterData!!)
            }
            Util.listScreen.size!=0 -> {
                screenData=ScreenUploadData(Util.listScreen,pd!!.clientName,
                    pd!!.moneyGet,pd!!.phoneNumber,pd!!.location,pd!!.date,
                    binding!!.screenMoneyRest.text.toString(),
                    binding!!.screenMoneyGet.text.toString(),"",binding!!.workName.text.toString())
                basketViewModel.uploadScreen(screenData!!)
            }
            else->{
                createPDF()
            }
        }
    }

    override fun onMusiceAddedFailed() {
        makeToastError()
    }

    override fun onTheaterAddedSuccess() {
        Util.theaterBolean=true
        when(Util.listScreen.size){
            0->{
                createPDF()
            }
            1->{
                screenData=ScreenUploadData(Util.listScreen,pd!!.clientName,
                    pd!!.moneyGet,pd!!.phoneNumber,pd!!.location,pd!!.date,
                    binding!!.screenMoneyRest.text.toString(),
                    binding!!.screenMoneyGet.text.toString(),"",binding!!.workName.text.toString())
                    basketViewModel.uploadScreen(screenData!!)
            }
        }
    }

    override fun onTheaterAddedFailed() {
        makeToastError()
    }

    override fun onScreenAddedSuccess() {
        Util.screenBolean=true
            createPDF()
    }

    override fun onScreenAddedFailed() {
        makeToastError()
    }
    fun soundTotalPrice():Double{
        var sum=0.0
        for(item in Util.soundList)sum+=item.price.toDouble()
        return sum
    }
    fun photoTotalPrice():Double{
        var sum=0.0
        for(item in Util.list)sum+=item.price.toDouble()
        return sum
    }
    fun theaterTotalPrice():Double{
        var sum=0.0
        for(item in Util.theaterList)sum+=item.price.toDouble()
        return sum
    }
    fun screenTotalPrice():Double{
        var sum=0.0
        for(item in Util.listScreen)sum+=item.price.toDouble()
        return sum
    }
}
