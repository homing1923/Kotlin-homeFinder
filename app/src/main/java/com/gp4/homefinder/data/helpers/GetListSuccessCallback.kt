package com.gp4.homefinder.data.helpers

import com.gp4.homefinder.data.models.Campus
import com.gp4.homefinder.data.models.House

interface GetListSuccessCallback {
    fun getListSuccessCallback(currentCampus:Campus, retrievedHouseList:MutableList<House>)
}