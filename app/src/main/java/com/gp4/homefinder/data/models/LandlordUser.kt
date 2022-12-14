package com.gp4.homefinder.data.models

class LandlordUser(
    var houseList: MutableList<House> = mutableListOf()
): User() {
}