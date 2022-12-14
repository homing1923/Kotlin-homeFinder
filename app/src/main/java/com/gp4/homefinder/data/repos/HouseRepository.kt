package com.gp4.homefinder.data.repos

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.gp4.homefinder.data.models.House
import com.gp4.homefinder.data.models.Campus

class HouseRepository {

    //fb
    private val db = Firebase.firestore
    private val collectionName = "users"

    private val myData = db.collection(collectionName)

    var houseLiveData: MutableLiveData<MutableList<House>> = MutableLiveData<MutableList<House>>()

    fun getListByCampus(campus: Campus){
        try{
            myData
                .document(campus.SchoolName)
                .collection(campus.campus)
                .addSnapshotListener(EventListener{snapshot, err ->
                    if(err != null){
                        Log.d("D1", "getListByCampus: addSnapshotListener err $err")
                    }
                    if(snapshot != null){
                        val localListOfHouse: MutableList<House> = if(houseLiveData.value == null){
                            mutableListOf()
                        }else{
                            houseLiveData.value as MutableList<House>
                        }
                        for (documentChange in snapshot.documentChanges) {

                            val currentHouse: House = documentChange.document.toObject(House::class.java)
                            currentHouse.id = documentChange.document.id

                            when (documentChange.type) {
                                DocumentChange.Type.ADDED -> {
                                    localListOfHouse.add(currentHouse)
                                }
                                DocumentChange.Type.MODIFIED -> {
                                    for((i, each) in houseLiveData.value!!.withIndex()){
                                        if(currentHouse.id == each.id){
                                            localListOfHouse[i] = currentHouse
                                        }
                                    }
                                }
                                DocumentChange.Type.REMOVED -> {
                                    localListOfHouse.remove(currentHouse)
                                }
                            }
                        }
                    }

                })
        }catch(err:java.lang.Exception){
            when(err){
                else -> Log.d("D1", "getListByCampus Exception : $err")
            }
        }
    }

    fun addOneHouse(newhouse: House){
//        TODO("apply this to the add house fragment to create a list of distance for comparison")
//        val api: IAPIResponse = RetrofitInstance.retrofitService
//
//        viewLifecycleOwner.lifecycleScope.launch {
//            val DistanceResponseFromApi():DistanceResponse = api.getDistances()
//            Log.d("D1", "Fetched a list of size ${todoListFromApi.size}")
//
//            for (each in todoListFromApi){
//                todoList.add(each)
//            }
//            todoAdapter.notifyDataSetChanged()
//        }
        try{
            myData
                .document()
        }catch(err:java.lang.Exception){
            when(err){
                else -> Log.d("D1", "AddOneHouse Exception : $err")
            }
        }
    }

    fun oddOneHouseByCampus(context: Context, newHouse: House, campus: Campus){
        try{
            myData
                .document(campus.SchoolName)
                .collection(campus.campus)
                .add(newHouse.getPropertyMapData(newHouse))
                .addOnSuccessListener {
                    Toast.makeText(context, "Add House Successful",Toast.LENGTH_SHORT).show()
                    Log.d("D1", "AddOneHouseBy Campus Exception Successed")
                }
                .addOnFailureListener {
                    Log.d("D1", "AddOneHouseBy Campus Exception Failed")
                }
        }catch(err:java.lang.Exception){
            when(err){
                else -> Log.d("D1", "AddOneHouse Exception : $err")
            }
        }
    }
}