package com.example.kotlin_learning.data.api

import com.example.kotlin_learning.data.model.BayesRuleModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface BayesRuleApi {

    @GET("/BayesRule")
    suspend fun getBayesRulePara(
        @Query("pa") pa: Float,
        @Query("pb") pb: Float,
        @Query("pab") pab: Float
    ) : Response<BayesRuleModel>
}