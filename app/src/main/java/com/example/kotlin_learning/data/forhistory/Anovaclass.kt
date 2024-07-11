package com.example.kotlin_learning.data.forhistory

data class Anovaclass (
    val anovaid: String = "",
    val userid: String = "",
    val n: List<Float> = listOf(),
    val size: Int = 0,
    val sl: Float = 0f,
    val msb: Float = 0f,
    val msw: Float = 0f,
    val ssb: Float = 0f,
    val ssw: Float = 0f,
    val dfB: Int = 0,
    val dfW: Int = 0,
    val fratio: Float = 0f,
    val hypothesis: String = ""
)