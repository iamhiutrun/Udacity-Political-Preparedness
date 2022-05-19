package com.example.android.politicalpreparedness.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Update
import com.example.android.politicalpreparedness.network.models.Election
import kotlinx.coroutines.flow.Flow

@Dao
interface ElectionDao {

    @Insert(onConflict = REPLACE)
    fun insertAll(elections : List<Election>)

    @Query("SELECT * FROM election_table")
    fun getAllElections(): LiveData<List<Election>>

    @Query("SELECT * FROM election_table WHERE id=:id")
    fun getElection(id:Int): Election

    @Insert
    suspend fun insert(election: Election)

    @Query("DELETE FROM ELECTION_TABLE WHERE id=:id")
    suspend fun delete(id:Int)

    @Query("DELETE FROM election_table")
    suspend fun clear()

    @Update
    suspend fun update(election: Election)

    @Query("SELECT * FROM election_table WHERE isFollowed = 1")
    fun getFollowedElections(): LiveData<List<Election>>

}