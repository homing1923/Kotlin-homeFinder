package com.gp4.homefinder.data.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import com.gp4.homefinder.R
import com.gp4.homefinder.data.models.House
import com.gp4.homefinder.data.models.OnItemClickListener
import com.gp4.homefinder.databinding.HouseviewBinding

class HouseAdapter(private val context: Context, private val arrayList: MutableList<House>, private val onItemClickListener: OnItemClickListener):
    RecyclerView.Adapter<HouseAdapter.HouseAdapterViewHolder>() {
        inner class HouseAdapterViewHolder(var binding:HouseviewBinding):RecyclerView.ViewHolder(binding.root){
            fun bind(currentItem: House, onItemClickListener: OnItemClickListener ){
                binding.houseViewAddress.text = currentItem.address
                binding.houseViewPeople.text = currentItem.maxCapacity.toString()
                binding.houseViewRent.text = currentItem.rentalCost.toString()
                binding.typeChip.text = currentItem.houseType
                binding.houseViewHydroChip.isChecked = currentItem.includeHydro
                if(!currentItem.includeHydro){
                    binding.houseViewHydroChip.chipBackgroundColor = ColorStateList.valueOf(
                        ContextCompat.getColor(context, R.color.TerraCotta_2))
                }else{
                    binding.houseViewHydroChip.chipBackgroundColor = ColorStateList.valueOf(
                        ContextCompat.getColor(context, R.color.GreenSheen_11))
                }
                binding.houseViewHeatChip.isChecked = currentItem.includeHeat
                if(!currentItem.includeHeat){
                    binding.houseViewHeatChip.chipBackgroundColor = ColorStateList.valueOf(
                        ContextCompat.getColor(context, R.color.TerraCotta_2))
                }else{
                    binding.houseViewHeatChip.chipBackgroundColor = ColorStateList.valueOf(
                        ContextCompat.getColor(context, R.color.GreenSheen_11))
                }
                binding.houseViewWaterChip.isChecked = currentItem.includeWater
                if(!currentItem.includeWater){
                    binding.houseViewWaterChip.chipBackgroundColor = ColorStateList.valueOf(
                        ContextCompat.getColor(context, R.color.TerraCotta_2))
                }else{
                    binding.houseViewWaterChip.chipBackgroundColor = ColorStateList.valueOf(
                        ContextCompat.getColor(context, R.color.GreenSheen_11))
                }
                if(!currentItem.allowPet){
                    binding.houseViewPetChip.chipBackgroundColor = ColorStateList.valueOf(
                        ContextCompat.getColor(context, R.color.TerraCotta_2))
                }else{
                    binding.houseViewPetChip.chipBackgroundColor = ColorStateList.valueOf(
                        ContextCompat.getColor(context, R.color.GreenSheen_11))
                }
                if(!currentItem.allowSmoke){
                    binding.houseViewSmokeChip.chipBackgroundColor = ColorStateList.valueOf(
                        ContextCompat.getColor(context, R.color.TerraCotta_2))
                }else{
                    binding.houseViewSmokeChip.chipBackgroundColor = ColorStateList.valueOf(
                        ContextCompat.getColor(context, R.color.GreenSheen_11))
                }


                itemView.setOnClickListener {
                    onItemClickListener.onHouseClick(currentItem)
                }
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HouseAdapterViewHolder {
        return (HouseAdapterViewHolder(HouseviewBinding.inflate(LayoutInflater.from(context))))
    }

    override fun onBindViewHolder(holder: HouseAdapterViewHolder, position: Int) {
        holder.bind(arrayList.get(position), onItemClickListener)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }
}