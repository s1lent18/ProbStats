package com.example.kotlin_learning.data.forhistory

data class Hypothesisclass (
    val hypothesisId: String = "",
    val userId: String = "",
    val smean: Float = 0f,
    val hmean: Float = 0f,
    val n: Int = 0,
    val sd: Float = 0f,
    val sl: Float = 0f,
    val tail: Boolean = false,
    val samplem: Boolean = false,
    val hypothesis: String = ""
)