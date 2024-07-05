package com.example.kotlin_learning.data.api

import com.example.kotlin_learning.data.model.HypothesisModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface HypothesisApi {

    @GET("/Hypothesis")
    suspend fun getHypothesisPara(
        @Query("smean") smean: Float,
        @Query("hmean") hmean: Float,
        @Query("n") n : Int,
        @Query("sd") sd: Float,
        @Query("sl") sl: Float,
        @Query("tail") tail: Boolean,
        @Query("samplem") samplem: Boolean

    ) : Response<HypothesisModel>
}