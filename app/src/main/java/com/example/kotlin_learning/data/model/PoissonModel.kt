package com.example.kotlin_learning.data.model

import com.google.gson.annotations.SerializedName

data class PoissonModel(
    @SerializedName("equal") val equal: Float,
    @SerializedName("greater") val greater: Float,
    @SerializedName("greaterequal") val greaterequal: Float,
    @SerializedName("less") val less: Float,
    @SerializedName("lessequal") val lessequal: Float
)