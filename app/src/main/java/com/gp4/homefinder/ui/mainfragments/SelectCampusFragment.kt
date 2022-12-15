package com.gp4.homefinder.ui.mainfragments

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.OnTokenCanceledListener
import com.gp4.homefinder.R
import com.gp4.homefinder.data.DataSource
import com.gp4.homefinder.data.LocationHelper
import com.gp4.homefinder.data.adapter.CampusAdapter
import com.gp4.homefinder.data.api.IAPIResponse
import com.gp4.homefinder.data.api.RetrofitInstance
import com.gp4.homefinder.data.models.Campus
import com.gp4.homefinder.data.models.House
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
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var currentLocation: Location


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
        binding.selectCampusRdgOpt2Lv.adapter = listViewAdapter
        binding.selectCampusRdgOpt2Lv.layoutManager = LinearLayoutManager(requireContext())
        geocoder = Geocoder(context, Locale.getDefault())
        listViewAdapter.notifyDataSetChanged()
        binding.selectCampusRdgOpt2Lv.isEnabled = false
        val navHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.mainFragmentContainer) as NavHostFragment
        this.navController = navHostFragment.navController
        setListener()
    }

    private fun setListener(){
        binding.selectCampusRdg.setOnCheckedChangeListener { radioGroup, i ->
            when(i){
                R.id.select_campus_rdg_opt1 -> {
                    dataSource.currentCampus = null
                    binding.selectCampusRdgOpt1Edt.isEnabled = true
                    binding.btnSearchCampus.isEnabled = true
                    binding.btnUseCurrentCampus.isEnabled = true
                    listViewAdapter.isClickable = false
                    if(binding.selectCampusRdgOpt1Edt.text.isNullOrEmpty()){
                        "Enter an Address".also { binding.selectCampusSelectedCampus.text = it}
                    }else{
                        binding.selectCampusSelectedCampus.text = binding.selectCampusRdgOpt1Edt.text
                    }
                }
                R.id.select_campus_rdg_opt2 -> {
                    dataSource.currentCampus = null
                    binding.selectCampusRdgOpt1Edt.isEnabled = false
                    binding.btnSearchCampus.isEnabled = false
                    binding.btnUseCurrentCampus.isEnabled = false
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
                        binding.selectCampusSelectedCampus.text =
                            "${dataSource.listOfCampus[minIndex].SchoolName}\n${dataSource.listOfCampus[minIndex].campus}\n${dataSource.listOfCampus[minIndex].address}"
                        dataSource.currentCampus = dataSource.listOfCampus[minIndex]
                        dataSource.currentCampusId = minIndex
                    }
                }

            }
        }

        binding.btnUseCurrentCampus.setOnClickListener {
            val locationHelper: LocationHelper = LocationHelper.instance
            locationHelper.checkPermissions(requireContext())
            if(locationHelper.locationPermissionGranted){
                getCurrentLocation()
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

    override fun onCampusClick(index:Int, campus: Campus, isClickable: Boolean) {
        if(isClickable){
            "Selected: ${campus.SchoolName}, ${campus.campus}\nAddress: ${campus.address}".also { binding.selectCampusSelectedCampus.text = it }
            dataSource.currentCampus = campus
            dataSource.currentCampusId = index
        }
    }

    override fun onHouseClick(house: House) {
        //Nothing to do
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

    private fun performForwardGeocoding(context: Context, location: com.google.android.gms.maps.model.LatLng) {
        Log.d("D1", "LocationHelper: doing forwardGeocode")
        geocoder = Geocoder(context, Locale.getDefault())
        val decodedLocation = geocoder.getFromLocation(location.latitude, location.longitude,1)
        if(decodedLocation != null){
            Log.d("D1", "LocationHelper: forwardGeocode success")
            binding.selectCampusRdgOpt1Edt.setText(decodedLocation[0].postalCode)
        }else{
            Log.d("D1", "Decoding Address error")
        }
    }

    @SuppressLint("MissingPermission")
    fun getCurrentLocation(){
        val locationHelper: LocationHelper = LocationHelper.instance
        val locationRequest : CurrentLocationRequest = CurrentLocationRequest.Builder().build()
        locationHelper.checkPermissions(requireContext())
        if (locationHelper.locationPermissionGranted){
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
            fusedLocationClient.getCurrentLocation(locationRequest, object : CancellationToken(){
                override fun onCanceledRequested(p0: OnTokenCanceledListener): CancellationToken {
                    return this
                }
                override fun isCancellationRequested(): Boolean {
                    return false
                }

            })
                .addOnSuccessListener {location ->
                    if (location != null){
                        currentLocation = location
                        performForwardGeocoding(requireContext(), LatLng(currentLocation!!.latitude, currentLocation!!.longitude) )
                        Log.e("D1", "getCurrentLocation: current location obtained : ${currentLocation}" )
                    }else{
                        Log.e("D1", "getCurrentLocation: Not able to get last known location" )
                    }
                    return@addOnSuccessListener
                }
        }
    }

}