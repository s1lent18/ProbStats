package com.example.kotlin_learning.data.response

sealed class NetworkResponse<out T> {
    data class Success<out T>(val data: T) : NetworkResponse<T>()
    data class Failure(val msg: String) : NetworkResponse<Nothing>()
    data object Loading : NetworkResponse<Nothing>()
}