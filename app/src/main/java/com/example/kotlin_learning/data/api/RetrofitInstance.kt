package com.example.kotlin_learning.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private const val BASEURL = "https://probstats.pythonanywhere.com/"

    private fun getInstance(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASEURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val SLRapi: SLRApi = getInstance().create(SLRApi::class.java)
    val anovaapi: AnovaApi = getInstance().create(AnovaApi::class.java)
    val poissonapi: PoissonApi = getInstance().create(PoissonApi::class.java)
    val groupedapi: GroupedApi = getInstance().create(GroupedApi::class.java)
    val binomialapi: BinomialApi = getInstance().create(BinomialApi::class.java)
    val bayesruleapi: BayesRuleApi = getInstance().create(BayesRuleApi::class.java)
    val ungroupedapi: UnGroupedApi = getInstance().create(UnGroupedApi::class.java)
    val hypothesisapi: HypothesisApi = getInstance().create(HypothesisApi::class.java)
    val multinomialapi: MultinomialApi = getInstance().create(MultinomialApi::class.java)
}