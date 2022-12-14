package com.gp4.homefinder.ui.mainfragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.gp4.homefinder.databinding.FragmentMapsBinding


class MapsFragment : Fragment(), OnMapReadyCallback {
    //map
    private lateinit var mMap: GoogleMap
    private lateinit var mapView: MapView
    private lateinit var locationToShow : LatLng

    private lateinit var navController: NavController

    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMapsBinding.inflate(LayoutInflater.from(context), container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView.getMapAsync(this)
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(locationToShow, 8.0F))
//        mMap.addMarker(MarkerOptions().position(locationToShow).title(args.country.capital))
        mMap.mapType = GoogleMap.MAP_TYPE_TERRAIN
        val uiSettings = googleMap.uiSettings
        uiSettings.isMyLocationButtonEnabled = true
        uiSettings.isCompassEnabled = true
        uiSettings.isMapToolbarEnabled = true
        uiSettings.isZoomControlsEnabled = true
    }
}