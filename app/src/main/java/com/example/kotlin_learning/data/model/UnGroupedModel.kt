package com.example.kotlin_learning.data.model

data class UnGroupedModel(
    val Shape_of_the_Distribution: String,
    val mean: Float,
    val median: Float,
    val mode: String,
    val one: Float,
    val q1: Float,
    val q3: Float,
    val sd: Float,
    val stemleaf: Map<String, FloatArray>,
    val three: Float,
    val two: Float,
    val variance: Float
)