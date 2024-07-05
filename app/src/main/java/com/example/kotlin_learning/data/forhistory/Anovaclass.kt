package com.example.kotlin_learning.data.forhistory

data class Anovaclass (
    val anovaid: String = "",
    val userid: String = "",
    val n: List<Float> = listOf(),
    val size: Int = 0,
    val sl: Float = 0f,
    val MSB: Float = 0f,
    val MSW: Float = 0f,
    val SSB: Float = 0f,
    val SSW: Float = 0f,
    val dfB: Int = 0,
    val dfW: Int = 0,
    val fratio: Float = 0f,
    val hypothesis: String = ""
)