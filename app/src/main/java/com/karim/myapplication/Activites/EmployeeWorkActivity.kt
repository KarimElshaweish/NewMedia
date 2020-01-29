package com.karim.myapplication.Activites

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.karim.myapplication.Adapter.workAdapter
import com.karim.myapplication.Interfaces.OnWorkDoneLoadedLisntner
import com.karim.myapplication.R
import com.karim.myapplication.Util
import com.karim.myapplication.ViewModel.WorkDoneViewModel
import kotlinx.android.synthetic.main.activity_employee_work.*
import kotlinx.android.synthetic.main.activity_employee_work.add_fab
import kotlinx.android.synthetic.main.fragment_work_done.*
import kotlinx.android.synthetic.main.fragment_work_done.workDonePb as workDonePb1

class EmployeeWorkActivity : AppCompatActivity(),OnWorkDoneLoadedLisntner {

    var workDoneModel: WorkDoneViewModel = WorkDoneViewModel()
    lateinit var adapter: workAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_employee_work)
        var txt=" الموظف "
        val name:String=intent.getStringExtra("name").toString()
        txt+=name
        employeName.text=txt
        add_fab.setOnClickListener{
            val intent=Intent(this,AddWorkDone::class.java)
            intent.putExtra("name",name)
            startActivity(intent)
        }
        if(!Util.empolyee) {
            workDoneModel= ViewModelProviders.of(this).get(WorkDoneViewModel::class.java)
            workDoneModel.init(this,name)
            adapter = workAdapter(this, workDoneModel.getEmployeeWorkDoneList().value!!)
            val rvWork = findViewById<RecyclerView>(R.id.rv)
            rvWork.setHasFixedSize(true)
            rvWork.layoutManager = LinearLayoutManager(this)
            rvWork.adapter = adapter
        }else{
            workDonePb.visibility = View.GONE
            cv.visibility=View.GONE
        }
    }
    fun back(view: View) {
        finish()
    }

    override fun onWorkDoneLoadSuccefully() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onWorkDoneLoadedFaield() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onEmployeeWorkDoneLoadedSuccessfully() {
        workDoneModel.getEmployeeWorkDoneList().observe(this,
            Observer<Any> {
               workDonePb.visibility = View.GONE
                adapter.notifyDataSetChanged()
            })
    }
    override fun onEmployeeWorkDoneLoadedFailed() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onEmployesLoadSuccess() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onEmployesLoadFailed() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
