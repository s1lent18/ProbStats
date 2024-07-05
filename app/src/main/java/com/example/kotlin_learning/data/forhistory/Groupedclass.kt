package com.example.kotlin_learning.data.forhistory

data class Groupedclass (
    val groupedId: String = "",
    val userId: String = "",
    val first: List<Float> = listOf(),
    val second: List<Float> = listOf(),
    val freq: List<Float> = listOf(),
    val mean: Float = 0f,
    val median: String = "",
    val mode: String = "",
    val sd: Float = 0f,
    val variance: Float = 0f
)