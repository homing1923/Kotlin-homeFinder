package com.gp4.homefinder.data.helpers

import com.gp4.homefinder.data.models.House

interface HouseCreateSuccessCallback {
    fun houseCreateSuccessCallback(id: String, newHouse: House)
}