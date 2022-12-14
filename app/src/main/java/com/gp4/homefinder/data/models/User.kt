package com.gp4.homefinder.data.models

import java.util.*

enum class userType{
    SHARE,
    RENT,
    NEW
}

open class User (
    var id: String = UUID.randomUUID().toString(),
    var email: String = "",
    var nickname: String = "",
    var address: String = "",
    var userType: String? = null,
    var campus: Campus? = null,
    var listOfHouse: MutableList<House> = mutableListOf(),
){

}