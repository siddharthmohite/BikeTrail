package com.j18.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query


@Dao
interface MyDataAccessObject {

    @Query("Select * from Recent_Trails order by time desc limit 5")
    fun fetchRecentTrials(): LiveData<List<Trail>>

    @Query("Select id from Recent_Trails order by time desc")
    fun fetchRecentTrialIds(): LiveData<List<String>>


    @Query("Select * from Recent_Trails where id=(:id)")
    fun fetchTrial(id:String): LiveData<Trail?>

    @Insert
    fun addRecentTrail(trail: Trail)
}