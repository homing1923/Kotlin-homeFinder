package com.gp4.homefinder.data.models

class TenantUser(
    var favourList:MutableList<House> = mutableListOf(),
): User() {

}