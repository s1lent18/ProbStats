package com.example.kotlin_learning.data.api

import com.example.kotlin_learning.data.model.SLRModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SLRApi {

    @GET("/SLR")
    suspend fun getSLRPara(
        @Query("n") n: Int,
        @Query("x") x: FloatArray,
        @Query("y") y: FloatArray,
        @Query("alpha") alpha: Float,
        @Query("tail") tail: Boolean
    ) : Response<SLRModel>
}