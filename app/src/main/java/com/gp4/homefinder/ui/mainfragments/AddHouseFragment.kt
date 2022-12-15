package com.gp4.homefinder.ui.mainfragments

import android.app.DatePickerDialog
import android.app.Dialog
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gp4.homefinder.R
import com.gp4.homefinder.data.DataSource
import com.gp4.homefinder.data.adapter.HouseAdapter
import com.gp4.homefinder.data.helpers.HouseAddToUserSuccessCallback
import com.gp4.homefinder.data.helpers.HouseCreateSuccessCallback
import com.gp4.homefinder.data.models.House
import com.gp4.homefinder.data.repos.HouseRepository
import com.gp4.homefinder.data.repos.UserRepository
import com.gp4.homefinder.databinding.FragmentAddHouseBinding
import java.time.LocalDate

class AddHouseFragment : Fragment(), HouseCreateSuccessCallback, HouseAddToUserSuccessCallback {
    private lateinit var navController: NavController

    private var _binding: FragmentAddHouseBinding? = null
    val binding get() = _binding!!

    private lateinit var dataSource: DataSource
    private lateinit var houseAdapter: HouseAdapter

    private lateinit var houseRepository: HouseRepository
    private lateinit var userRepository: UserRepository

    private var newHouseType:String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddHouseBinding.inflate(layoutInflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        houseRepository = HouseRepository()
        userRepository = UserRepository()
        dataSource = DataSource.getInstance()
        val navHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.mainFragmentContainer) as NavHostFragment
        this.navController = navHostFragment.navController
        attachListener()
        attachSpinner()
        setUpSpinnerOnClick()
    }

    private fun attachSpinner() {
        val houseTypeSpinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, House.houseType)
        val campusSpinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, dataSource.simpleCampusList)
        binding.addTypeSpinner.adapter = houseTypeSpinnerAdapter
        binding.addCampusEdt.adapter = campusSpinnerAdapter
        newHouseType = House.houseType[0]
    }

    private fun attachListener(){
        binding.addInfoContainer2Sub1HydroCheckbox.setOnClickListener {
            binding.addInfoContainer2Sub1HydroEdt.isEnabled = !binding.addInfoContainer2Sub1HydroCheckbox.isChecked
        }
        binding.addInfoContainer2Sub2HeatCheckbox.setOnClickListener {
            binding.addInfoContainer2Sub2HeatEdt.isEnabled = !binding.addInfoContainer2Sub2HeatCheckbox.isChecked
        }
        binding.addInfoContainer2Sub3WaterCheckbox.setOnClickListener {
            binding.addInfoContainer2Sub3WaterEdt.isEnabled = !binding.addInfoContainer2Sub3WaterCheckbox.isChecked
        }
        binding.addInfoDateEdt.setOnFocusChangeListener { view, b ->
            if(b){
                DatePickerFragment().show((requireParentFragment().childFragmentManager), "DatePicker For House Create")
            }
        }
        binding.createHouseBtn.setOnClickListener {
            validateData()
        }
    }

    private fun validateData() {
        var validated = true
        var newHouseAddress: String? = null
        var newHouseIncludeHydro: Boolean? = null
        var newHouseIncludeHeat: Boolean? = null
        var newHouseIncludeWater: Boolean? = null
        var newHouseHydroCost: Float = 0.0F
        var newHouseHeatCost: Float = 0.0F
        var newHouseWaterCost: Float = 0.0F
        var newHouseRentalCost: Float = 0.0F
        var newHouseMaxCapacity: Int? = null
        val newHouseAllowPet: Boolean = binding.addInfoContainer3Sub1PetCheckbox.isChecked
        val newHouseAllowSmoke: Boolean = binding.addInfoContainer3Sub2SmokeCheckbox.isChecked
        var newHouseAvailabilty:String? = null
        

        if(binding.addAddressEdt.text.isNullOrBlank()){
            validated = false
            binding.addAddressEdt.error = "Please enter an address"
        }else{
            newHouseAddress = binding.addAddressEdt.text.toString()
        }
        if(binding.addPeopleEdt.text.isNullOrBlank()){
            validated = false
            binding.addPeopleEdt.error = "Please enter people required"
        }else{
            newHouseMaxCapacity = binding.addPeopleEdt.text.toString().toInt()
        }
        if(binding.addRentEdt.text.isNullOrBlank()){
            validated = false
            binding.addRentEdt.error = "Please enter a rent"
        }else{
            newHouseRentalCost = binding.addRentEdt.text.toString().toFloat()
        }
        if(!binding.addInfoContainer2Sub1HydroCheckbox.isChecked){
            if(binding.addInfoContainer2Sub1HydroEdt.text.isNullOrEmpty()){
                validated = false
                binding.addInfoContainer2Sub1HydroEdt.error = "Please enter amount"
            }else{
                newHouseIncludeHydro = false
                newHouseHydroCost = binding.addInfoContainer2Sub1HydroEdt.text.toString().toFloat()
            }
        }else{
            newHouseIncludeHydro = true
        }
        if(!binding.addInfoContainer2Sub2HeatCheckbox.isChecked){
            if(binding.addInfoContainer2Sub2HeatEdt.text.isNullOrEmpty()){
                validated = false
                binding.addInfoContainer2Sub2HeatEdt.error = "Please enter amount"
            }else{
                newHouseIncludeHeat = false
                binding.addInfoContainer2Sub2HeatEdt.text.toString().toFloat()
                    .also { newHouseHeatCost = it }
            }
        }else{
            newHouseIncludeHeat = true
        }
        if(!binding.addInfoContainer2Sub3WaterCheckbox.isChecked){
            if(binding.addInfoContainer2Sub3WaterEdt.text.isNullOrEmpty()){
                validated = false
                binding.addInfoContainer2Sub3WaterEdt.error = "Please enter amount"
            }else{
                newHouseIncludeWater = false
                newHouseWaterCost = binding.addInfoContainer2Sub3WaterEdt.text.toString().toFloat()
            }
        }else{
            newHouseIncludeWater = true
        }
        if(binding.addInfoDateEdt.text.isNullOrBlank()){
            validated = false
            binding.addInfoDateEdt.error = "Please select a date"
        }else{
            newHouseAvailabilty = binding.addInfoDateEdt.text.toString()
        }

        if(newHouseType == null){
            validated = false
        }

            
        if(validated){
            houseRepository.oddOneHouseByCampus(
                requireContext(),
                House(
                    address = newHouseAddress!!,
                    houseType = newHouseType!!,
                    rentalCost = newHouseRentalCost,
                    hydroCost = newHouseHydroCost,
                    waterCost = newHouseWaterCost,
                    heatCost = newHouseHeatCost,
                    maxCapacity = newHouseMaxCapacity!!,
                    availabilty = newHouseAvailabilty!!,
                    allowSmoke = newHouseAllowSmoke,
                    allowPet = newHouseAllowPet,
                    includeHydro = newHouseIncludeHydro!!,
                    includeWater = newHouseIncludeWater!!,
                    includeHeat = newHouseIncludeHeat!!,
                    createdByUser = dataSource.currentUser!!.email
                ),
                dataSource.currentCampus!!,
                this
            )

        }

    }

    private fun setUpSpinnerOnClick(){
        binding.addTypeSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, id: Long) {
                newHouseType = House.houseType[position]
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
//                Log.d("D1", "onNothingSelected: User has not made any selection. Set the default percentage to 0%")
            }
        }
        binding.addCampusEdt.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, id: Long) {
                dataSource.currentCampus = dataSource.listOfCampus[position]
                dataSource.currentCampusId = position
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
//                Log.d("D1", "onNothingSelected: User has not made any selection. Set the default percentage to 0%")
            }
        }

    }

    override fun houseCreateSuccessCallback(id: String, newHouse:House) {
        userRepository.addHouseToUser(id, newHouse, this )
    }

    override fun houseAddSuccessCallback() {
        navController.navigate(R.id.houseListingFragment)
    }
}


class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener{
    val TAG = "DatePickerFragment"

    lateinit var dataSource: DataSource

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val today = LocalDate.now()
        val todayYear = today.year
        val todayMonth = today.monthValue -1
        val todayDay = today.dayOfMonth
        return DatePickerDialog(this.requireActivity(), this, todayYear, todayMonth, todayDay)
    }

    override fun onDateSet(p0: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        dataSource = DataSource.getInstance()
        var myDate = LocalDate.of(year, month+1, dayOfMonth)
        Log.d(TAG, "onDateSet: Date selection ${myDate}")
        dataSource.dateForHouseCreate = myDate
        (requireParentFragment().childFragmentManager.fragments[0] as AddHouseFragment).binding.addInfoDateEdt.setText("${myDate.year} ${myDate.month} ${myDate.dayOfMonth}")
    }
}