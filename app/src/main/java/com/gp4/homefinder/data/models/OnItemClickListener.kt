package com.gp4.homefinder.data.models

interface OnItemClickListener {
    fun onCampusClick(index:Int, campus: Campus, isClickable: Boolean)
    fun onHouseClick(house:House)
}