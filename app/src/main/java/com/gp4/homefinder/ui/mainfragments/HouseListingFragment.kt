package com.gp4.homefinder.ui.mainfragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.navigation.NavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.gp4.homefinder.data.DataSource
import com.gp4.homefinder.data.models.House
import com.gp4.homefinder.data.adapter.HouseAdapter
import com.gp4.homefinder.data.helpers.GetListSuccessCallback
import com.gp4.homefinder.data.models.Campus
import com.gp4.homefinder.data.models.OnItemClickListener
import com.gp4.homefinder.data.repos.HouseRepository
import com.gp4.homefinder.databinding.FragmentHouseListingBinding
import com.gp4.homefinder.ui.activity.MainActivity

class HouseListingFragment : Fragment(), OnItemClickListener, GetListSuccessCallback {
    val TAG = "D1"

    private lateinit var navController: NavController

    private var _binding: FragmentHouseListingBinding? = null
    private val binding get() = _binding!!

    private lateinit var houseRepository: HouseRepository

    private lateinit var houseListAdapter:HouseAdapter
    private var localHouseList: MutableList<House> = mutableListOf()

    private lateinit var dataSource:DataSource
    private lateinit var spinnerAdapter: ArrayAdapter<String>

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

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

        spinnerAdapter = initializeSpinner((activity as MainActivity), android.R.layout.simple_spinner_item, dataSource.simpleCampusList)
        binding.houseListingSpinner.adapter = spinnerAdapter
        if(dataSource.currentCampusId !== null){
            binding.houseListingSpinner.setSelection(dataSource.currentCampusId!!)
        }

        houseListAdapter = HouseAdapter(requireContext(), localHouseList, this)
        binding.houseListingLv.adapter = houseListAdapter
        binding.houseListingLv.layoutManager = GridLayoutManager(requireContext(), 2)
        houseListAdapter.notifyDataSetChanged()

        swipeRefreshLayout = binding.swipeLayout
        swipeRefreshLayout.setOnRefreshListener {
            loadListByCampus()
            swipeRefreshLayout.isRefreshing = false
        }

        setUpSpinnerOnClick()

    }

    private fun loadListByCampus() {
        Log.d(TAG, "HouseListingFragment.onResumed : Start observe")
        if(dataSource.currentCampus !== null){
            houseRepository.getListByCampus(this, dataSource.currentCampus!!)
            houseListAdapter.notifyDataSetChanged()
        }else{
            Log.d(TAG, "HouseListingFragment.onResumed : dataSource.currentCampus null")
        }
    }

    private fun initializeSpinner(mainActivity: MainActivity, simpleSpinnerItem: Int, listOfCampus: MutableList<String>):ArrayAdapter<String> {
        return ArrayAdapter(mainActivity, simpleSpinnerItem, listOfCampus)
    }

    private fun setUpSpinnerOnClick(){
        binding.houseListingSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, id: Long) {
                Log.d(TAG, "onItemSelected: User selection : ${dataSource.listOfCampus[position]}")
                dataSource.currentCampus = dataSource.listOfCampus[position]
                loadListByCampus()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                Log.d(TAG, "onNothingSelected: User has not made any selection. Set the default percentage to 0%")
            }
        }
    }

    override fun onCampusClick(index:Int, campus: Campus, isClickable: Boolean) {
        //DO Nothing
    }

    override fun onHouseClick(house: House) {
        //Navigate to map
    }

    override fun getListSuccessCallback(currentCampus: Campus, retrievedHouseList: MutableList<House>) {
        localHouseList.clear()
        localHouseList.addAll(retrievedHouseList)
        dataSource.currentCampus = currentCampus
        houseListAdapter.notifyDataSetChanged()
    }

}