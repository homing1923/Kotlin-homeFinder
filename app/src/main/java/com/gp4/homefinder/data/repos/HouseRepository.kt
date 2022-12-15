package com.gp4.homefinder.data.repos

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.gp4.homefinder.data.DataSource
import com.gp4.homefinder.data.helpers.GetListSuccessCallback
import com.gp4.homefinder.data.helpers.HouseCreateSuccessCallback
import com.gp4.homefinder.data.models.House
import com.gp4.homefinder.data.models.Campus

class HouseRepository {

    //fb
    private val db = Firebase.firestore
    private val collectionName = "houses"

    private lateinit var dataSource:DataSource

    private val myData = db.collection(collectionName)
    var houseLiveData: MutableLiveData<MutableList<House>> = MutableLiveData<MutableList<House>>()

    fun getListByCampus(cb: GetListSuccessCallback, campus: Campus){
        dataSource = DataSource.getInstance()
        try{
            myData
                .document(campus.SchoolName)
                .collection(campus.campus)
                .get()
                .addOnSuccessListener {
                    val newList = mutableListOf<House>()
                    it.documents.forEach { snapshot ->
                        val newHouse:House? = snapshot.toObject(House::class.java)
                        if(newHouse !== null){
                            newList.add(newHouse)
                        }else{
                            Log.d("D1", "getListByCampus: fail to cast house")
                        }

                    }
                    cb.getListSuccessCallback(campus, newList)
                }
//                .addSnapshotListener(EventListener{snapshot, err ->
//                    if(err != null){
//                        Log.d("D1", "getListByCampus: addSnapshotListener err $err")
//                    }
//                    if(snapshot != null){
//                        val localListOfHouse: MutableList<House> = if(houseLiveData.value == null){
//                            mutableListOf()
//                        }else{
//                            houseLiveData.value as MutableList<House>
//                        }
//                        for (documentChange in snapshot.documentChanges) {
//
//                            val currentHouse: House = documentChange.document.toObject(House::class.java)
//                            currentHouse.id = documentChange.document.id
//
//                            when (documentChange.type) {
//                                DocumentChange.Type.ADDED -> {
//                                    localListOfHouse.add(currentHouse)
//                                    Log.d("D1", "getListByCampus: addone")
//                                }
//                                DocumentChange.Type.MODIFIED -> {
//                                    for((i, each) in houseLiveData.value!!.withIndex()){
//                                        if(currentHouse.id == each.id){
//                                            localListOfHouse[i] = currentHouse
//                                        }
//                                    }
//                                    Log.d("D1", "getListByCampus: change one")
//                                }
//                                DocumentChange.Type.REMOVED -> {
//                                    localListOfHouse.remove(currentHouse)
//                                    Log.d("D1", "getListByCampus: remove one")
//                                }
//                            }
//                            houseLiveData.postValue(localListOfHouse)
//                        }
//                    }
//
//                })
        }catch(err:java.lang.Exception){
            when(err){
                else -> Log.d("D1", "getListByCampus Exception : $err")
            }
        }
    }

    fun addOneHouse(campusId:Int, newhouse: House){
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

    fun oddOneHouseByCampus(context: Context, newHouse: House, campus: Campus, houseCreateSuccessCallback: HouseCreateSuccessCallback){
        dataSource = DataSource.getInstance()
        try{
            myData
                .document(campus.SchoolName)
                .collection(campus.campus)
                .add(newHouse.getPropertyMapData(newHouse))
                .addOnSuccessListener {
                    Toast.makeText(context, "Add House Successful",Toast.LENGTH_SHORT).show()
                    houseCreateSuccessCallback.houseCreateSuccessCallback(it.id, newHouse)
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