package com.gp4.homefinder.data.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gp4.homefinder.data.models.Campus
import com.gp4.homefinder.data.models.OnItemClickListener
import com.gp4.homefinder.databinding.CampusviewBinding


class CampusAdapter(private val context: Context, private val arrayList: MutableList<Campus>, private val onItemClickListener: OnItemClickListener):
    RecyclerView.Adapter<CampusAdapter.CampusAdapterViewHolder>() {
    var isClickable: Boolean = false
    inner class CampusAdapterViewHolder(var binding: CampusviewBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(index:Int, currentItem: Campus, onItemClickListener: OnItemClickListener){
            "${currentItem.campus} campus".also { binding.campusViewCampus.text = it }
            "Address: ${currentItem.address}".also { binding.campusViewAddress.text = it }
            currentItem.SchoolName.also { binding.campusViewTitle.text = it }

            itemView.setOnClickListener {
                onItemClickListener.onCampusClick(index, currentItem, isClickable)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CampusAdapterViewHolder {
        return (CampusAdapterViewHolder(CampusviewBinding.inflate(LayoutInflater.from(context), parent, false)))
    }

    override fun onBindViewHolder(holder: CampusAdapterViewHolder, position: Int) {
        holder.bind(position, arrayList.get(position), onItemClickListener)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }
}