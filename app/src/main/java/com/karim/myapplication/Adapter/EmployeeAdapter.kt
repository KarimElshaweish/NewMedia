package com.karim.myapplication.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import com.karim.myapplication.Activites.EmployeeWorkActivity
import com.karim.myapplication.R
import com.karim.myapplication.Util
import com.karim.myapplication.model.EmployeeData

class EmployeeAdapter(var employeesList:MutableList<EmployeeData>,var _ctx:Context)
    :RecyclerView.Adapter<EmployeeAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var employeeName = view.findViewById<TextView>(R.id.employeName_item)
        var employeeRl = view.findViewById<RelativeLayout>(R.id.employeeRl)
        var deleteImage = view.findViewById<ImageView>(R.id.img_delete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.employee_item,
                parent,
                false
            )
        )
    }
    override fun getItemCount() = employeesList.size
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val employee = employeesList[position]
        holder.employeeName.text = employee.name
        if (Util.empolyee)
            holder.deleteImage.visibility = View.GONE
        holder.deleteImage.setOnClickListener {
            deleteEmployee(employee.id)
        }
        holder.employeeRl.setOnClickListener{
            var intent=Intent(_ctx,EmployeeWorkActivity::class.java)
            intent.putExtra("id",employee.id)
            intent.putExtra("name",employee.name)
            _ctx.startActivity(intent)
        }
    }

    fun deleteEmployee(id: String) {
        val ref = FirebaseDatabase.getInstance().reference
        val query = ref.child("employees")
        query.child(id).setValue(null).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(_ctx, "تم حذف الموظف", Toast.LENGTH_SHORT).show()
                this.notifyDataSetChanged()
            } else {
                Toast.makeText(_ctx, _ctx.getString(R.string.error), Toast.LENGTH_SHORT).show()
            }
        }
    }
}