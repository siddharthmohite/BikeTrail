package com.j18.trailbuddy

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TrailApiInterface {
    @GET("explore/")
    fun fetchBikeTrails(@Query("lat") lat : Double, @Query("lon") lon : Double): Call<String>

    @GET("{bikeTrailId}")
    fun fetchBikeTrailsInfo(@Path("bikeTrailId") id: Int?): Call<String>

    @GET("{bikeTrailId}/maps/")
    fun fetchBikeTrailsMapList(@Path("bikeTrailId") id: Int?): Call<String>
}