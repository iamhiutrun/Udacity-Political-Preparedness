package com.example.android.politicalpreparedness.di

import android.content.Context
import androidx.room.RoomDatabase
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.repository.CivicsRepository
import com.example.android.politicalpreparedness.repository.ElectionDataSource
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CivicsModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context) =
        ElectionDatabase.getInstance(appContext).electionDao

    @Provides
    fun provideDispatcher() = Dispatchers.IO

}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule{
    @Singleton
    @Binds
    abstract fun bindRepository(civicsRepository: CivicsRepository): ElectionDataSource
}