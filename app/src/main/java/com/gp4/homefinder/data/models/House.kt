package com.gp4.homefinder.data.models

import android.util.Log
import java.time.Instant
import java.time.LocalDate
import java.util.Date
import java.util.UUID
import kotlin.reflect.full.*

data class House (
    var address:String = "",
    var houseType: String = "DEFAULT",
    var rentalCost:Float = 0.0F,
    var hydroCost:Float = 0.0F,
    var waterCost:Float = 0.0F,
    var heatCost:Float = 0.0F,
    var maxCapacity:Int = 0,
    var availabilty:String = Date.from(Instant.now()).toString().slice(0..9),
    var allowSmoke:Boolean = false,
    var allowPet:Boolean = false,
    var includeHydro:Boolean = false,
    var includeWater:Boolean = false,
    var includeHeat:Boolean = false,
    var id:String = UUID.randomUUID().toString(),
){
    companion object{
        var houseType:ArrayList<String> = arrayListOf(
            "HOUSE", "CONDO", "APARTMENT", "BASEMENT"
        )
    }
    fun getPropertyMapData(some:Any):HashMap<String,Any>{
        val map:HashMap<String,Any> = HashMap()
        some.javaClass.kotlin.memberProperties.forEach {
            map[it.name] = it.call(some) as Any
        }
        Log.d("D1", "map data obtained = $map")
        return map
    }

}