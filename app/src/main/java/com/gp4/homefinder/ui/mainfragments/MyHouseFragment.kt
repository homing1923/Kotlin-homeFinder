package com.gp4.homefinder.ui.mainfragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gp4.homefinder.R
import com.gp4.homefinder.data.adapter.HouseAdapter
import com.gp4.homefinder.data.helpers.GetAllCreatedHouseSuccessCallback
import com.gp4.homefinder.data.models.Campus
import com.gp4.homefinder.data.models.House
import com.gp4.homefinder.data.models.OnItemClickListener
import com.gp4.homefinder.databinding.FragmentMyHouseBinding
import com.gp4.homefinder.databinding.FragmentSelectCampusBinding

class MyHouseFragment : Fragment(), GetAllCreatedHouseSuccessCallback, OnItemClickListener {

    private var _binding: FragmentMyHouseBinding? = null
    private val binding get() = _binding!!

    private lateinit var houseAdapter: HouseAdapter
    private var localMyHouseList:MutableList<House> = mutableListOf()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMyHouseBinding.inflate(layoutInflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        attachAdapter()
    }

    private fun attachAdapter() {
        houseAdapter = HouseAdapter(requireContext(), localMyHouseList, this)
        binding
    }

    override fun getAllSuccess(listOfHouse: MutableList<House>) {
        TODO("Not yet implemented")
    }

    override fun onCampusClick(index: Int, campus: Campus, isClickable: Boolean) {
        //Do Nothing
    }

    override fun onHouseClick(house: House) {
        TODO("Not yet implemented")
    }
}