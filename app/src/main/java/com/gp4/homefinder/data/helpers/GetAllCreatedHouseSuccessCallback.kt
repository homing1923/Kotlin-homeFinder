package com.gp4.homefinder.data.helpers

import com.gp4.homefinder.data.models.House

interface GetAllCreatedHouseSuccessCallback {
    fun getAllSuccess(listOfHouse:MutableList<House>)
}