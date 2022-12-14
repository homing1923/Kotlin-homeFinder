package com.gp4.homefinder.ui.splashfragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.FragmentController
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.gp4.homefinder.data.DataSource
import com.gp4.homefinder.R
import com.gp4.homefinder.databinding.FragmentEntryBinding

class EntryFragment : Fragment() {
    private var _binding: FragmentEntryBinding? = null
    private val binding get() = _binding!!

    lateinit var dataSource: DataSource
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentEntryBinding.inflate(layoutInflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataSource = DataSource.getInstance()
        val navHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.container1) as NavHostFragment
        this.navController = navHostFragment.navController
        binding.splashSigninBtn.setOnClickListener{
            val action = EntryFragmentDirections.actionEntryFragmentToSignInFragment()
            this.navController.navigate(action)
        }
        binding.splashSignupBtn.setOnClickListener{
            val action = EntryFragmentDirections.actionEntryFragmentToSignUpFragment()
            this.navController.navigate(action)
        }
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().finish()
            }
        }
        requireActivity().getOnBackPressedDispatcher().addCallback(viewLifecycleOwner, onBackPressedCallback)

    }

    companion object {

    }
}