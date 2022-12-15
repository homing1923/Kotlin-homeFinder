package com.gp4.homefinder.data.models

import java.util.*

open class User (
    var id: String = UUID.randomUUID().toString(),
    var email: String = "",
    var nickname: String = "",
    var address: String = "",
    var userType: String? = null,
    var campusId: Int? = null,
    var listOfHouse: MutableList<House> = mutableListOf(),
){

}