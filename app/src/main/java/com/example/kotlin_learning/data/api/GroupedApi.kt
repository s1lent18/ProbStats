package com.example.kotlin_learning.data.api

import com.example.kotlin_learning.data.model.GroupedModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GroupedApi {

    @GET("/Grouped")
    suspend fun getGroupedPara(
        @Query("first") first: FloatArray,
        @Query("second") second: FloatArray,
        @Query("freq") freq: FloatArray
    ) : Response<GroupedModel>
}