package com.j18.db

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.j18.trailbuddy.search_fragment
import java.util.concurrent.Executors


private const val TAG = "MyDatabaseRepository"
private const val DATABASE_NAME = "trail-database"
class MyDatabaseRepository(private val fragment: Fragment){


    private val database:MyDatabase= Room.databaseBuilder(
        fragment.requireContext().applicationContext,
        MyDatabase::class.java,
        DATABASE_NAME
    )
        .fallbackToDestructiveMigration()
        .build()
    private val myDao = database.myDao()

    private val executor = Executors.newSingleThreadExecutor()

    private fun fetchRecentTrialIds(): LiveData<List<String>> = myDao.fetchRecentTrialIds()

    fun fetchTrails():LiveData<List<Trail>> = myDao.fetchRecentTrials()

    fun fetchTrail(id: String): LiveData<Trail?> = myDao.fetchTrial(id)

    fun addTrail(trail: Trail)
    {
        this.executor.execute{
            this.myDao.addRecentTrail(trail)
        }
    }


}