package com.example.kotlin_learning.data.api

import com.example.kotlin_learning.data.model.UnGroupedModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface UnGroupedApi {

    @GET("/Ungrouped")
    suspend fun getUnGroupedPara (
        @Query("n") n: FloatArray

    ) : Response<UnGroupedModel>

}