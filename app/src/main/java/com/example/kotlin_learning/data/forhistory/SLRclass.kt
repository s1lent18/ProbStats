package com.example.kotlin_learning.data.forhistory

data class SLRclass (
    val slrid: String = "",
    val userId: String = "",
    val n: Int = 0,
    val x: List<Float> = listOf(),
    val y: List<Float> = listOf(),
    val alpha: Float = 0f,
    val tail: Boolean = false,
    val hypothesis: String = "",
    val r: Float = 0f,
    val t: Float = 0f,
    val yy: String = ""
)
