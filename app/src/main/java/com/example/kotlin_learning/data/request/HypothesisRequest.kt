package com.example.kotlin_learning.data.request

class HypothesisRequest (
    val smean: Float,
    val hmean: Float,
    val n: Int,
    val sd: Float,
    val sl: Float,
    val tail: Boolean,
    val samplem: Boolean
)