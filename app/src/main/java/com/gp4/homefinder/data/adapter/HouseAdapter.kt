package com.gp4.homefinder.data.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gp4.homefinder.data.models.House
import com.gp4.homefinder.databinding.HouseviewBinding

class HouseAdapter(private val context: Context,private val arrayList: MutableList<House>):
    RecyclerView.Adapter<HouseAdapter.HouseAdapterViewHolder>() {
        inner class HouseAdapterViewHolder(var binding:HouseviewBinding):RecyclerView.ViewHolder(binding.root){
            fun bind(currentItem: House){
//                binding.
                TODO("Bind views")
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HouseAdapterViewHolder {
        return (HouseAdapterViewHolder(HouseviewBinding.inflate(LayoutInflater.from(context))))
    }

    override fun onBindViewHolder(holder: HouseAdapterViewHolder, position: Int) {
        holder.bind(arrayList.get(position))
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }
}