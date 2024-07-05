package com.example.kotlin_learning.data.api

import com.example.kotlin_learning.data.model.MultinomialModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MultinomialApi {

    @GET("/Multinomial")
    suspend fun getMultinomialPara (
        @Query("n") n: Float,
        @Query("x") x: FloatArray,
        @Query("p") p: FloatArray
    ) : Response<MultinomialModel>
}