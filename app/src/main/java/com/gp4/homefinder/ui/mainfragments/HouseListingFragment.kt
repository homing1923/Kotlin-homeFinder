package com.gp4.homefinder.ui.mainfragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import com.gp4.homefinder.data.DataSource
import com.gp4.homefinder.data.models.House
import com.gp4.homefinder.data.adapter.HouseAdapter
import com.gp4.homefinder.data.models.Campus
import com.gp4.homefinder.data.repos.HouseRepository
import com.gp4.homefinder.databinding.FragmentHouseListingBinding
import com.gp4.homefinder.ui.activity.MainActivity

class HouseListingFragment : Fragment() {
    val TAG = "D1"

    private lateinit var navController: NavController

    private var _binding: FragmentHouseListingBinding? = null
    private val binding get() = _binding!!

    private lateinit var houseRepository: HouseRepository

    private lateinit var houseListAdapter:HouseAdapter
    private var localHouseList: MutableList<House> = mutableListOf()

    private lateinit var dataSource:DataSource
    private lateinit var spinnerAdapter: ArrayAdapter<Campus>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHouseListingBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataSource = DataSource.getInstance()
        houseRepository = HouseRepository()

        spinnerAdapter = initializeSpinner((activity as MainActivity), android.R.layout.simple_spinner_item, dataSource.listOfCampus)
        binding.houseListingSpinner.adapter = spinnerAdapter
        setUpSpinnerOnClick()

        houseListAdapter = HouseAdapter(activity as MainActivity, localHouseList)
        binding.houseListingLv.adapter = houseListAdapter

        if(dataSource.currentCampus !== null){
            houseRepository.getListByCampus(dataSource.currentCampus!!)
            houseRepository.houseLiveData.observe(activity as MainActivity, Observer { CampusListFromServer ->
                localHouseList.clear()
                CampusListFromServer.forEach {
                    localHouseList.add((it))
                }
                houseListAdapter.notifyItemRangeChanged(0, localHouseList.size)
            })
        }else{
            Log.d(TAG, "HouseListingFragment.onCreated : dataSource.currentCampus null")
        }
    }

    private fun initializeSpinner(mainActivity: MainActivity, simpleSpinnerItem: Int, listOfCampus: MutableList<Campus>):ArrayAdapter<Campus> {
        return ArrayAdapter(mainActivity, simpleSpinnerItem, listOfCampus)
    }

    private fun setUpSpinnerOnClick(){
        binding.houseListingSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, id: Long) {
                Log.d(TAG, "onItemSelected: User selection : ${dataSource.listOfCampus[position]}")
                dataSource.currentCampus = dataSource.listOfCampus[position]
                TODO("UPDATE LISTVIEW BASE ON USER SELECTION")
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                Log.d(TAG, "onNothingSelected: User has not made any selection. Set the default percentage to 0%")
            }
        }
    }
}