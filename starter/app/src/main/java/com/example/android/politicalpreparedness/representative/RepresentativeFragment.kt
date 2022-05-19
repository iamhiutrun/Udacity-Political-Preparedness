package com.example.android.politicalpreparedness.representative

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresPermission
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.example.android.politicalpreparedness.base.BaseFragment
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentRepresentativeBinding
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.representative.adapter.RepresentativeListAdapter
import com.example.android.politicalpreparedness.utils.LocationPermissionsUtil
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.OnTokenCanceledListener
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class RepresentativeFragment : BaseFragment() {

    lateinit var binding: FragmentRepresentativeBinding
    override val viewModel: RepresentativeViewModel by viewModels()
    lateinit var fusedLocationClient: FusedLocationProviderClient

    @SuppressLint("MissingPermission")
    private val locationPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()){ isGranted->
        if(isGranted){
            getLocation()
        }else{
            Snackbar.make(binding.root, R.string.error_location_permission_denied, Snackbar.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentRepresentativeBinding.inflate(inflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.representativeContainer.setTransition(R.id.start, R.id.start)

        viewModel.representatives.observe(viewLifecycleOwner) {
            if (it.isNullOrEmpty()) {
                binding.representativeContainer.setTransition(R.id.start, R.id.start)
            } else {
                binding.representativeContainer.setTransition(R.id.start, R.id.end)
            }
        }

        //Set onclick
        binding.buttonLocation.setOnClickListener {
            handleGeo()
        }

        //adapter
        val adapter = RepresentativeListAdapter()
        binding.representativeRecycler.adapter = adapter

        //location initialization
        fusedLocationClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())

        viewModel.showLoading.observe(viewLifecycleOwner) {
            if (it) {
                hideKeyboard()
                binding.representativesLoading.visibility=View.VISIBLE
            }else{
                binding.representativesLoading.visibility=View.GONE
            }
        }


        binding.state.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                viewModel.state.value =
                    requireContext().resources.getStringArray(R.array.states)[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        return binding.root
    }

    private fun handleGeo() {
        if (!LocationPermissionsUtil.isPermissionGranted(requireContext())) {
            locationPermission.launch(LocationPermissionsUtil.PERMISSION)
        } else {
            getLocation()
        }
    }

    @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    private fun getLocation() {
        fusedLocationClient.getCurrentLocation(100, object : CancellationToken() {
            override fun isCancellationRequested(): Boolean {
                return false
            }

            override fun onCanceledRequested(p0: OnTokenCanceledListener): CancellationToken {
                return this
            }

        }).addOnSuccessListener {
            val address = geoCodeLocation(it)
            viewModel.getAddressFromGeoLocation(address)
        }
    }

    private fun geoCodeLocation(location: Location): Address {
        val geocoder = Geocoder(context, Locale.getDefault())
        return geocoder.getFromLocation(location.latitude, location.longitude, 1)
            .map { address ->
                Address(
                    address.thoroughfare,
                    address.subThoroughfare,
                    address.locality,
                    address.adminArea,
                    address.postalCode
                )
            }
            .first()
    }

    private fun hideKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }
}