package com.gp4.homefinder.ui.mainfragments

import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.gp4.homefinder.R
import com.gp4.homefinder.data.DataSource
import com.gp4.homefinder.data.adapter.CampusAdapter
import com.gp4.homefinder.data.api.IAPIResponse
import com.gp4.homefinder.data.api.RetrofitInstance
import com.gp4.homefinder.data.models.Campus
import com.gp4.homefinder.data.models.OnItemClickListener
import com.gp4.homefinder.databinding.FragmentSelectCampusBinding
import kotlinx.coroutines.launch
import java.util.*

class SelectCampusFragment : Fragment() , OnItemClickListener{
    private lateinit var navController: NavController

    private var _binding: FragmentSelectCampusBinding? = null
    private val binding get() = _binding!!

    private lateinit var dataSource:DataSource
    private lateinit var listViewAdapter: CampusAdapter

    private lateinit var geocoder: Geocoder


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSelectCampusBinding.inflate(layoutInflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataSource = DataSource.getInstance()
        listViewAdapter = CampusAdapter(requireContext(), dataSource.listOfCampus, this)
        Log.d("D1", "Loading list of campus in selectCampusFragment: size = ${dataSource.listOfCampus.size}")
        binding.selectCampusRdgOpt2Lv.adapter = listViewAdapter
        binding.selectCampusRdgOpt2Lv.layoutManager = LinearLayoutManager(requireContext())
        geocoder = Geocoder(context, Locale.getDefault())
        listViewAdapter.notifyDataSetChanged()
        binding.selectCampusRdgOpt2Lv.isEnabled = false
        val navHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.container2) as NavHostFragment
        this.navController = navHostFragment.navController
        setListener()
    }



    private fun setListener(){
        binding.selectCampusRdg.setOnCheckedChangeListener { radioGroup, i ->
            Log.d("D1", "RDG changed, RDGBTNid = ${i}")
            when(i){
                R.id.select_campus_rdg_opt1 -> {
                    dataSource.currentCampus = null
                    binding.selectCampusRdgOpt1Edt.isEnabled = true
                    binding.btnSearchCampus.isEnabled = true
                    listViewAdapter.isClickable = false
                    if(binding.selectCampusRdgOpt1Edt.text.isNullOrEmpty()){
                        "Enter an Address".also { binding.selectCampusTitle.text = it}
                    }else{
                        binding.selectCampusTitle.text = binding.selectCampusRdgOpt1Edt.text
                    }
                }
                R.id.select_campus_rdg_opt2 -> {
                    dataSource.currentCampus = null
                    binding.selectCampusRdgOpt1Edt.isEnabled = false
                    binding.btnSearchCampus.isEnabled = false
                    listViewAdapter.isClickable = true
                }
            }
        }
        binding.btnSearchCampus.setOnClickListener {
            if(!binding.selectCampusRdgOpt1Edt.text.isNullOrEmpty()){
                var resultOfResolvingAddress = doReverseGeocoding(binding.selectCampusRdgOpt1Edt.text.toString())
                if( resultOfResolvingAddress !== null){
                    val api: IAPIResponse = RetrofitInstance.retrofitService
                    viewLifecycleOwner.lifecycleScope.launch{
                        val apiResponse = api.getDistances(
                            "${resultOfResolvingAddress.latitude},${resultOfResolvingAddress.longitude}"
                            , dataSource.campusLatLngs)
                        val listOfDistances = mutableListOf<Int>()
                        var minIndex = -1
                        var minValue = 9999999
                        for((i, each) in apiResponse.rows[0].elements.withIndex()){
                            if(each.distance.value.toInt() < minValue){
                                minValue = each.distance.value.toInt()
                                minIndex = i
                            }
                            listOfDistances.add(each.distance.value.toInt())
                        }
                        binding.selectCampusTitle.text =
                            "${dataSource.listOfCampus[minIndex].SchoolName}\n${dataSource.listOfCampus[minIndex].campus}\n${dataSource.listOfCampus[minIndex].address}"
                        dataSource.currentCampus = dataSource.listOfCampus[minIndex]
                    }
                }

            }
        }
        binding.selectCampusSendBtn.setOnClickListener {
            if(dataSource.currentCampus !== null){
                val action = SelectCampusFragmentDirections.actionSelectCampusFragmentToHouseListingFragment()
                navController.navigate(action)
            }else{
                Toast.makeText(requireContext(), "Please select a campus", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onItemClick(campus: Campus, isClickable: Boolean) {
        if(isClickable){
            "Selected: ${campus.SchoolName}, ${campus.campus}\nAddress: ${campus.address}".also { binding.selectCampusTitle.text = it }
            dataSource.currentCampus = campus
        }
    }

    private fun doReverseGeocoding(address: String):com.google.android.gms.maps.model.LatLng? {
        Log.d("D1", "onClick: Perform reverse geocoding to get coordinates from address.")
        return if(address.isNotEmpty()){
            val convertedLocation = geocoder.getFromLocationName(address, 1)
            if(!convertedLocation.isNullOrEmpty()){
                com.google.android.gms.maps.model.LatLng(convertedLocation[0].latitude, convertedLocation[0].longitude)
            }else{
                null
            }
        }else{
            null
        }
    }

}