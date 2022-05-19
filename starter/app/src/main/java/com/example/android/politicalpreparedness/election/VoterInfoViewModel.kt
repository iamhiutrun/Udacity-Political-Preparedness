package com.example.android.politicalpreparedness.election

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.base.BaseViewModel
import com.example.android.politicalpreparedness.utils.Result
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.State
import com.example.android.politicalpreparedness.repository.CivicsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

//class VoterInfoViewModel(private val dataSource: ElectionDao) : ViewModel() {
//
//    //TODO: Add live data to hold voter info
//
//    //TODO: Add var and methods to populate voter info
//
//    //TODO: Add var and methods to support loading URLs
//
//    //TODO: Add var and methods to save and remove elections to local database
//    //TODO: cont'd -- Populate initial state of save button to reflect proper action based on election saved status
//
//    /**
//     * Hint: The saved state can be accomplished in multiple ways. It is directly related to how elections are saved/removed from the database.
//     */
//
//}

@HiltViewModel
class VoterInfoViewModel @Inject constructor(
    app: Application,
    private val repository: CivicsRepository
) :
    BaseViewModel(app) {

    //Added live data to hold voter info
    private val _voterInfo = MutableLiveData<State>()
    val voterInfo: LiveData<State>
        get() = _voterInfo

    //Added var and methods to populate voter info
    private val _election = MutableLiveData<Election>()
    val election: LiveData<Election>
        get() = _election


    //Added var and methods to support loading URLs
    private fun fetchVoterInfo() {
        viewModelScope.launch {
            val result = repository.getVoterInfo(election.value ?: return@launch)
            when (result) {
                is Result.Success -> {
                    _voterInfo.postValue(result.data)
                }
                is Result.Error -> {
                    showSnackBar.postValue(result.message)
                }
            }
        }
    }

    fun setElection(id: Int) {
        viewModelScope.launch {
            val election = withContext(Dispatchers.IO) { repository.getElection(id) }
            _election.value = election
            fetchVoterInfo()
        }
    }

    //Added var and methods to save and remove elections to local database
    //cont'd -- Populate initial state of save button to reflect proper action based on election saved status

    /**
     * Hint: The saved state can be accomplished in multiple ways. It is directly related to how elections are saved/removed from the database.
     */
    fun updateElection() {
        val election = _election.value ?: return
        election.isFollowed = !election.isFollowed
        viewModelScope.launch {
            repository.updateElection(election)
            _election.value = election
        }
    }

}