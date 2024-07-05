package com.example.kotlin_learning.data.api

import com.example.kotlin_learning.data.model.PoissonModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PoissonApi {

    @GET("/Poisson")
    suspend fun getPoissonPara (
        @Query("x") x: Float,
        @Query("lamda") lamda: Float
    ) : Response<PoissonModel>
}