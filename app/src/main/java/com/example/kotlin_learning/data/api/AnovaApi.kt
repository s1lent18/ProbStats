package com.example.kotlin_learning.data.api

import com.example.kotlin_learning.data.model.AnovaModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface AnovaApi {

    @GET("/Anova")
    suspend fun getAnovaPara(
        @Query("n") n: FloatArray,
        @Query("size") size: Int,
        @Query("sl") sl: Float
    ) : Response<AnovaModel>
}