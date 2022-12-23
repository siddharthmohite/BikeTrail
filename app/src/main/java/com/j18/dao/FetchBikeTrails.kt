package com.j18.dao

import com.google.gson.annotations.Expose

data class FetchBikeTrails public constructor(
    @Expose var id: String?,
    @Expose var name: String?,
    @Expose var url: String?,
    @Expose var length: String?,
    @Expose var description: String?,
    @Expose var city: String?,
    @Expose var region: String?,
    @Expose var country: String?,
    @Expose var lat: String?,
    @Expose var lon: String?,
    @Expose var difficulty: String?,
    @Expose var features: String?,
    @Expose var rating: String?,
    @Expose var thumbnail: String?
) {

    override fun toString(): String {
        return "FetchBikeTrails(id=$id, name=$name, url=$url, length=$length, description=$description, city=$city, region=$region, country=$country, lat=$lat, lon=$lon, difficulty=$difficulty, features=$features, rating=$rating, thumbnail=$thumbnail)"
    }




}