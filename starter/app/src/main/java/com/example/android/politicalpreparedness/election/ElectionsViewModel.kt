package com.example.android.politicalpreparedness.election

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.base.BaseViewModel
import com.example.android.politicalpreparedness.utils.NavigationCommand
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.repository.CivicsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

//TODO: Construct ViewModel and provide election datasource
//class ElectionsViewModel: ViewModel() {
//
//    //TODO: Create live data val for upcoming elections
//
//    //TODO: Create live data val for saved elections
//
//    //TODO: Create val and functions to populate live data for upcoming elections from the API and saved elections from local database
//
//    //TODO: Create functions to navigate to saved or upcoming election voter info
//
//}

@HiltViewModel
class ElectionsViewModel @Inject constructor(
    app: Application,
    private val repository: CivicsRepository
) : BaseViewModel(app) {

    //Created live data val for upcoming elections
    val upcomingElections: LiveData<List<Election>>
        get() = repository.electionsUpcoming

    //Created live data val for saved elections
    val followedElections: LiveData<List<Election>>
        get() = repository.electionsFollowed

    //Created val and functions to populate live data for upcoming elections from the API and saved elections from local database
    private fun refreshElections() {
        viewModelScope.launch {
            repository.refreshElectionsData()
        }
    }

    //Created functions to navigate to saved or upcoming election voter info
    fun navigateToVoterInfo(election: Election) {
        navigationCommand.value = NavigationCommand.To(
            ElectionsFragmentDirections.actionElectionsFragmentToVoterInfoFragment(
                election.id,
                election.division
            )
        )
    }

    init {
        refreshElections()
    }

}