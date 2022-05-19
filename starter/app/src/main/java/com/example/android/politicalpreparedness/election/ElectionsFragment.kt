package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.android.politicalpreparedness.base.BaseFragment
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import com.example.android.politicalpreparedness.election.adapter.ElectionListener
import com.example.android.politicalpreparedness.network.models.Election
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ElectionsFragment : BaseFragment(), ElectionListener {

    //TODO: Declare ViewModel
    lateinit var binding: FragmentElectionBinding
    override val viewModel : ElectionsViewModel by viewModels()
    private val upComingAdapter = ElectionListAdapter(this)
    private val followedAdapter = ElectionListAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //TODO: Add ViewModel values and create ViewModel
        //TODO: Add binding values
        binding = FragmentElectionBinding.inflate(inflater)
        //TODO: Link elections to voter info
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        //TODO: Initiate recycler adapters
        binding.rvUpcoming.adapter = upComingAdapter
        binding.rvSaved.adapter = followedAdapter
        //TODO: Populate recycler adapters
        return binding.root
    }

    override fun onClick(election: Election) {
        viewModel.navigateToVoterInfo(election)
    }

    //TODO: Refresh adapters when fragment loads

}