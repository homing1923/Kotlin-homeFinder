package com.gp4.homefinder.ui.mainfragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.gp4.homefinder.R
import com.gp4.homefinder.data.models.House
import com.gp4.homefinder.databinding.FragmentChooseBinding

class ChooseFragment : Fragment() {
    private lateinit var navController: NavController

    private var _binding: FragmentChooseBinding? = null
    private val binding get() = _binding!!

    private val localhouse: House = House()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChooseBinding.inflate(LayoutInflater.from(context), container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.mainFragmentContainer) as NavHostFragment
        this.navController = navHostFragment.navController
        binding.signupAsLandlordBtn.setOnClickListener{
            val action = ChooseFragmentDirections.actionChooseFragmentToAddHouseFragment()
            navController.navigate(action)
        }
        binding.signupAsTenantBtn.setOnClickListener{
            val action = ChooseFragmentDirections.actionChooseFragmentToSelectCampusFragment()
            navController.navigate(action)
        }
        localhouse.getPropertyMapData(localhouse)
    }

}