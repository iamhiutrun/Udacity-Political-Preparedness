package com.example.android.politicalpreparedness.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.android.politicalpreparedness.utils.Result
import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.State
import com.example.android.politicalpreparedness.representative.model.Representative
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CivicsRepository @Inject constructor(
    private val electionDao: ElectionDao,
    private val ioDispatcher:CoroutineDispatcher = Dispatchers.IO
) : ElectionDataSource {
    override val electionsFollowed: LiveData<List<Election>> = electionDao.getFollowedElections()
    override val electionsUpcoming: LiveData<List<Election>> = electionDao.getAllElections()

    override suspend fun getRepresentatives(address: Address): Result<List<Representative>> =
        withContext(ioDispatcher) {
            try {
                val response =
                    CivicsApi.retrofitService.fetchRepresentatives(address.toFormattedString())
                val representatives = response.mapResponseToRepresentatives()
                Result.Success(representatives)
            } catch (ex: Exception) {
                Result.Error(ex.localizedMessage)
            }

        }

    override suspend fun refreshElectionsData() {
        withContext(ioDispatcher) {
            try {
                val response = CivicsApi.retrofitService.fetchElections()
                val elections = response.elections
                electionDao.insertAll(elections)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override suspend fun updateElection(election: Election) = withContext(ioDispatcher) {
        try {
            electionDao.update(election)
        } catch (e: Exception) {
        }

    }

    override suspend fun getElection(id: Int): Election = electionDao.getElection(id)

    override suspend fun delete(election: Election) = withContext(ioDispatcher) {
        try {
            electionDao.delete(election.id)
        } catch (ex: Exception) {
        }
    }

    override suspend fun clear() = withContext(ioDispatcher) {
        try {
            electionDao.clear()
        } catch (ex: Exception) {
        }
    }

    override suspend fun getVoterInfo(election: Election): Result<State?> =
        withContext(ioDispatcher) {
            try {
                val response = CivicsApi.retrofitService.fetchVoterInfo(
                    election.division.toFormattedString(),
                    election.id
                ).state?.first()
                Log.d("TAG", "getVoterInfo: $response")
                Result.Success(response)
            } catch (ex: Exception) {
                Result.Error(ex.localizedMessage)
            }
        }

}