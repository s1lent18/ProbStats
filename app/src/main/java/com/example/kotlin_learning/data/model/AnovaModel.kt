package com.example.kotlin_learning.data.model

data class AnovaModel(
    val MSB: Float,
    val MSW: Float,
    val SSB: Float,
    val SSW: Float,
    val dfB: Int,
    val dfW: Int,
    val fratio: Float,
    val hypothesis: String
)