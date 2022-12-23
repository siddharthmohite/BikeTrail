package com.j18.utility

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.j18.dao.FetchBikeTrails
import com.j18.trailbuddy.TrailApiInterface
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.lang.reflect.Type


class RestApiCall {

    private fun getHttpClient(): OkHttpClient.Builder {
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor { chain ->
            val request: Request =
                chain.request().newBuilder().addHeader("X-RapidAPI-Host", "trailapi-trailapi.p.rapidapi.com")
                    .addHeader("X-RapidAPI-Key","90de3c61e8msh4e580b2d10c0fd4p17684bjsnead61cb7e42b")
                    .build()
            chain.proceed(request)
        }
        return httpClient
    }

    private fun getRetroFitObject(): TrailApiInterface {
        val retrofit: Retrofit =
            Retrofit.Builder().addConverterFactory(ScalarsConverterFactory.create())
                .baseUrl("https://trailapi-trailapi.p.rapidapi.com/trails/")
                .client(getHttpClient().build()).build()
        return retrofit.create(TrailApiInterface::class.java)

    }

    fun fetchBikeTrailsWrapper(latitude:Double, longitude:Double): MutableLiveData<String> {
        val responseLiveData: MutableLiveData<String> = MutableLiveData()
        val fetchBikeTrailResponse : Call<String> = getRetroFitObject().fetchBikeTrails(latitude,longitude)

        fetchBikeTrailResponse.enqueue(object : Callback<String> {
            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.i(ContentValues.TAG, "Failed to fetch bike trail response", t)
            }
            override fun onResponse(
                call: Call<String>,
                response: Response<String>
            ) {
               responseLiveData.value = response.body()
            }
        })
        return responseLiveData
    }

    fun fetchBikeTrailsInfoWrapper(bikeTrailId : Int): MutableLiveData<String> {
        val responseLiveData: MutableLiveData<String> = MutableLiveData()
        val fetchBikeTrailInfoResponse : Call<String> = getRetroFitObject().fetchBikeTrailsInfo(bikeTrailId)

        fetchBikeTrailInfoResponse.enqueue(object : Callback<String> {
            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.i(ContentValues.TAG, "Failed to fetch bike trail Info response", t)
            }
            override fun onResponse(call: Call<String>, response: Response<String>) {
                responseLiveData.value = response.body()
            }
        })
        return responseLiveData
    }

    fun fetchBikeTrailsMapListWrapper(bikeTrailId : Int): MutableLiveData<String> {
        val responseLiveData: MutableLiveData<String> = MutableLiveData()
        val fetchBikeTrailMapListResponse : Call<String> = getRetroFitObject().fetchBikeTrailsMapList(bikeTrailId)

        fetchBikeTrailMapListResponse.enqueue(object : Callback<String> {
            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.i(ContentValues.TAG, "Failed to fetch bike trail map list response", t)
            }
            override fun onResponse(call: Call<String>, response: Response<String>) {
                responseLiveData.value = response.body()
            }
        })
        return responseLiveData
    }

}