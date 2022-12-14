package com.gp4.homefinder.data.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.gp4.homefinder.R
import com.gp4.homefinder.data.models.Campus
import com.gp4.homefinder.data.models.OnItemClickListener
import com.gp4.homefinder.databinding.CampusviewBinding
import org.checkerframework.checker.units.qual.s


class CampusAdapter(private val context: Context, private val arrayList: MutableList<Campus>, private val onItemClickListener: OnItemClickListener):
    RecyclerView.Adapter<CampusAdapter.CampusAdapterViewHolder>() {
    var isClickable: Boolean = false
//    private var selectedPos = RecyclerView.NO_POSITION
    inner class CampusAdapterViewHolder(var binding: CampusviewBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(currentItem: Campus, onItemClickListener: OnItemClickListener){
            "${currentItem.campus} campus".also { binding.campusViewCampus.text = it }
            "Address: ${currentItem.address}".also { binding.campusViewAddress.text = it }
            currentItem.SchoolName.also { binding.campusViewTitle.text = it }

            itemView.setOnClickListener {
                onItemClickListener.onItemClick(currentItem, isClickable)
//                if (getAdapterPosition() == RecyclerView.NO_POSITION) return@setOnClickListener;
//                notifyItemChanged(selectedPos)
//                selectedPos = layoutPosition
//                notifyItemChanged(selectedPos)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CampusAdapterViewHolder {
        return (CampusAdapterViewHolder(CampusviewBinding.inflate(LayoutInflater.from(context), parent, false)))
    }

    override fun onBindViewHolder(holder: CampusAdapterViewHolder, position: Int) {
        holder.bind(arrayList.get(position), onItemClickListener)
//        if(isClickable){
//            if(selectedPos == position){
//                holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.DarkCoral_9))
//            }
//        }
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }
}