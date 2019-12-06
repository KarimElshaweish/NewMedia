package com.karim.myapplication.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.karim.myapplication.Model.TypesPackages
import com.karim.myapplication.R
import kotlinx.android.synthetic.main.spinner_custome_item.view.*

class SpinnerCustomeAdapter(ctx: Context,
                            types: List<TypesPackages>) :
    ArrayAdapter<TypesPackages>(ctx, 0, types) {

    override fun getView(position: Int, recycledView: View?, parent: ViewGroup): View {
        return this.createView(position, recycledView, parent)
    }
    override fun getDropDownView(position: Int, recycledView: View?, parent: ViewGroup): View {
        return this.createView(position, recycledView, parent)
    }
    private fun createView(position: Int, recycledView: View?, parent: ViewGroup): View {
        val type = getItem(position)
        val view = recycledView ?: LayoutInflater.from(context).inflate(
            R.layout.spinner_custome_item,
            parent,
            false
        )
        if (type != null) {
            view.package_image.setImageResource(type.image)
        }
        if (type != null) {
            view.package_name.text = type.name
        }
        return view
    }

}
