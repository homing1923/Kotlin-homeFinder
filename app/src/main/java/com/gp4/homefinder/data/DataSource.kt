package com.gp4.homefinder.data

import com.gp4.homefinder.data.models.Campus
import com.gp4.homefinder.data.models.User
import java.time.LocalDate

class DataSource(
    val listOfSchools: List<String> = listOf(
        "Ryerson University",
        "University of Toronto",
        "George Brown College",
        "York University",
        "Humber International Graduate School",
        "Humber College",
        "Seneca College",
        "OCAD University",
        "Centennial College",
        "University of Guelph-Humber",
        "Tyndale University"
    ),
    val listOfCampus: MutableList<Campus> = mutableListOf(
    Campus("Ryerson University","Main", "350 Victoria St, Toronto, ON M5B 2K3", 43.6582986,-79.3807846, postal = "M5B 2K3"),
    Campus("University of Toronto","Main", "27 King's College Cir, Toronto, ON M5S 1A1",43.660928,-79.3958992,postal = "M5S 1A1"),
    Campus("George Brown College","St James", "200 King's St, Toronto, M5A 3W8",43.6512708,-79.3698075,postal = "M5A 3W8"),
    Campus("George Brown College","Waterfront", "51 Dockside Dr, Toronto, ON M5A 0B6",43.6439625,-79.3654755,postal = "M5A 0B6"),
    Campus("George Brown College","Casa Loma", "160 Kendal Ave, Toronto, ON M5R 1M3",43.6761356,-79.4108209, postal = "M5R 1M3"),
    Campus("York University","Glendon", "2275 Bayview Ave, North York, ON M4N 3M6", 43.728213,-79.3780376,postal = "M4N 3M6"),
    Campus("York University","Main", "4700 Keele St, Toronto, ON M3J 1P3", 43.7750059, -79.4942155, postal = "M3J 1P3"),
    Campus("Humber College","Downtown", "59 Hayden St Unit 400, Toronto, ON M4Y 2P2",43.6698255,-79.3838049,postal = "M4Y 2P2"),
    Campus("Humber College","North", "205 Humber College Blvd, Etobicoke, ON M9W 5L7",43.7292505,-79.606896,postal = "M9W 5L7"),
    Campus("Humber College","Lake shore", "2 Colonel Samuel Smith Park Dr, Etobicoke, ON M8V 4B6",43.5976274,-79.517959,postal = "M8V 4B6"),
    Campus("Seneca College","Newnham", "1760 Finch Ave E, North York, ON M2J 5G3",43.7962262,-79.3467023,postal = "M2J 5G3"),
    Campus("Seneca College","Markham", "8 The Seneca Way, Markham, ON L3R 5Y1",43.8500619,-79.3671473,postal = "L3R 5Y1"),
    Campus("Seneca College","York", "70 The Pond Rd, North York, ON M3J 3M6",43.7712899,-79.499827,postal = "M3J 3M6"),
    Campus("Seneca College","Yorkgate", "1 York Gate Blvd, North York, ON M3N 3A1",43.7589862,-79.5188033,postal = "M3N 3A1"),
    Campus("OCAD University","Main","100 McCaul St, Toronto, ON M5T 1W1",43.6531845,-79.3914842,postal = "M5T 1W1"),
    Campus("Centennial College","East York","951 Carlaw Ave, Toronto, ON M4K 3M2",43.6847261,-79.3488439,postal = "M4K 3M2"),
    Campus("Centennial College","Progress","941 Progress Ave, Scarborough, ON M1G 3T8",43.7850417,-79.2270519,postal = "M1G 3T8"),
    Campus("Centennial College","Morningside","755 Morningside Ave, Scarborough, ON M1C 5J9",43.7863202,-79.1931192,postal = "M1C 5J9"),
    Campus("Centennial College","Astonbee","75 Ashtonbee Rd., Toronto, ON M1L 4N4",43.7308091,-79.2901252,postal = "M1L 4N4"),
    Campus("Centennial College","Downsview","65 Carl Hall Road, Toronto, ON M3K 2C1",43.7472362,-79.4758375,postal = "M3K 2C1"),
    Campus("University of Guelph-Humber","Main","207 Humber College Blvd, Etobicoke, ON M9W 5L7",43.7279758,-79.6060173,postal = "M9W 5L7"),
    Campus("Tyndale University","Main","3377 Bayview Ave, North York, ON M2M 3S4",43.7967694,-79.3921565,postal = "M2M 3S4")
    ),
    var mapOfCampus: MutableMap<String, MutableList<Campus>> = mutableMapOf(),
    var campusLatLngs: String = "",
    var simpleCampusList:MutableList<String> = mutableListOf(),
)
{
    init{
        for(each in listOfSchools){
            mapOfCampus[each] = mutableListOf()
        }
        for((i,each) in listOfCampus.withIndex()){
            if(mapOfCampus[each.SchoolName] !== null){
                mapOfCampus[each.SchoolName]!!.add(each)
                campusLatLngs += "${each.lat},${each.lng}"
                if(i != listOfCampus.size -1){
                    campusLatLngs += "|"
                }
            }
            simpleCampusList.add("${each.SchoolName} - ${each.campus}")
        }
    }

    companion object {
        @Volatile
        private lateinit var instance: DataSource

        fun getInstance(): DataSource {
            synchronized(this) {
                if (!::instance.isInitialized) {
                    instance = DataSource()
                }
                return instance
            }
        }
    }

    //Global variables
    var currentUser: User? = null
    var currentCampus: Campus? = null
    var currentCampusId: Int? = null
    var dateForHouseCreate: LocalDate? = null
    var currentHouseList: MutableList<Campus>? = mutableListOf()
}