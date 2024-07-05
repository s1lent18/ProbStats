package com.example.kotlin_learning.data.api

import com.example.kotlin_learning.data.model.BinomialModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface BinomialApi {

    @GET("/Binomial")
    suspend fun getBinomialPara (
        @Query("n") n: Float,
        @Query("x") x: Float,
        @Query("p") p: Float
    ) : Response<BinomialModel>
}