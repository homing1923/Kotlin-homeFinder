package com.gp4.homefinder.data.repos

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.gp4.homefinder.data.DataSource
import com.gp4.homefinder.data.helpers.GetAllCreatedHouseSuccessCallback
import com.gp4.homefinder.data.helpers.HouseAddToUserSuccessCallback
import com.gp4.homefinder.data.helpers.HouseCreateSuccessCallback
import com.gp4.homefinder.data.models.House


class UserRepository {
    private val TAG = "D1"

    //fb
    private val db = Firebase.firestore
    private val collectionName = "users"
    private val houseCollectionName = "houses"
    private val nickname = "nickname"
    private val address = "address"
    private val email = "email"
    private val myData = db.collection(collectionName)

    //local storage
    private lateinit var dataSource: DataSource
    private lateinit var prefs: SharedPreferences
    private var prefName:String = "com.gp4.homefinder"

    //Sign Up Event
    fun addUserToDb(context: Context, newUser: FirebaseUser){
        prefs =  context.getSharedPreferences(prefName, AppCompatActivity.MODE_PRIVATE)
        dataSource = DataSource.getInstance()
        myData.let { it ->
            //mapping data to json obj
            val data: MutableMap<String, Any> = HashMap()
            val emptyuser = com.gp4.homefinder.data.models.User()

            //map and create initial user data set
            data[nickname] = newUser.email!!.subSequence(0,newUser.email!!.indexOf("@"))
            data[address] = emptyuser.address
            data[email] = newUser.email.toString()

            if(dataSource.currentUser == null){
                it
                    .document(newUser.uid)
                    .set(data)
                    .addOnSuccessListener { doc ->
                        //save uid to pref for future use
                        prefs.edit().putString("USER_ID", newUser.uid).apply()

                        //local datasource short hand storage
                        dataSource.currentUser!!.id = newUser.uid
                        dataSource.currentUser!!.email = newUser.email.toString()
                        Log.d(TAG, "Data added with id: ${newUser.uid}")
                    }
                    .addOnFailureListener { err ->
                        Log.d(TAG, "Problem: $err")
                    }
                }
            //fb set data using fb auth uid as key

            }
        }

    fun addHouseToUser(newHouseId:String, newHouse:House, houseAddToUserSuccessCallback: HouseAddToUserSuccessCallback){
        dataSource = DataSource.getInstance()
        myData
            .document(dataSource.currentUser!!.id)
            .collection(houseCollectionName)
            .document(newHouseId)
            .set(newHouse)
            .addOnSuccessListener {
                houseAddToUserSuccessCallback.houseAddSuccessCallback()
            }
    }

    fun getAllCreatedHouse(getAllCreatedHouseSuccessCallback: GetAllCreatedHouseSuccessCallback){
        myData
            .document(dataSource.currentUser!!.id)
            .collection(houseCollectionName)
            .get()
            .addOnSuccessListener {
                val newHouselist:MutableList<House> = mutableListOf()
                it.documents.forEach { each ->
                    each.toObject(House::class.java).also { newHouse->
                        if(newHouse !== null){
                            newHouselist.add(newHouse)
                        }else{
                            Log.d("D1", "getAllCreatedHouse: Cast to house failed")
                        }
                    }
                }
                getAllCreatedHouseSuccessCallback.getAllSuccess(newHouselist)
            }
    }

    //Sign In Event
    fun getUserData(context: Context){
        //
        prefs =  context.getSharedPreferences(prefName, AppCompatActivity.MODE_PRIVATE)
        dataSource = DataSource.getInstance()
        db.collection(collectionName)
            .document(prefs.getString("USER_ID", "NA")!!)
            .get().addOnSuccessListener {
            var retrievedUser = it.toObject(com.gp4.homefinder.data.models.User::class.java)!!
            dataSource.currentUser = retrievedUser
            dataSource.currentUser!!.id = prefs.getString("USER_ID",null)!!
            return@addOnSuccessListener
        }

    }

    fun saveUserDataToDb( d_nickname:String,  d_address:String ) {
        dataSource = DataSource.getInstance()
        val data: MutableMap<String, Any> = HashMap()
        Log.d("D1", "Edit with data: id= ${dataSource.currentUser!!.id}")
        data[nickname] = d_nickname
        data[address] = d_address
        data[email] = dataSource.currentUser!!.email
        dataSource.currentUser?.let {
            db.collection(collectionName)
                .document(dataSource.currentUser!!.id)
                .collection("userdata")
                .add(data)
        }
    }
}