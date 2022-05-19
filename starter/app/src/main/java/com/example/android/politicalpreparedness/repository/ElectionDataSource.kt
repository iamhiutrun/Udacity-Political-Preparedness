package com.example.android.politicalpreparedness.repository

import androidx.lifecycle.LiveData
import com.example.android.politicalpreparedness.utils.Result
import com.example.android.politicalpreparedness.network.models.*
import com.example.android.politicalpreparedness.representative.model.Representative

interface ElectionDataSource {
    val electionsFollowed: LiveData<List<Election>>
    val electionsUpcoming: LiveData<List<Election>>

    suspend fun getRepresentatives(address: Address): Result<List<Representative>>
    suspend fun refreshElectionsData()
    suspend fun updateElection(election: Election)
    suspend fun getElection(id: Int): Election
    suspend fun delete(election: Election)
    suspend fun clear()
    suspend fun getVoterInfo(election: Election): Result<State?>
}