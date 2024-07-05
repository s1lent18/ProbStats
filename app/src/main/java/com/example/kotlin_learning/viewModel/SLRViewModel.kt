package com.example.kotlin_learning.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlin_learning.data.response.NetworkResponse
import com.example.kotlin_learning.data.api.RetrofitInstance
import com.example.kotlin_learning.data.model.SLRModel
import com.example.kotlin_learning.data.request.SLRRequest
import kotlinx.coroutines.launch

class SLRViewModel : ViewModel() {

    private val SLRapi = RetrofitInstance.SLRapi
    private val _SLRresult = MutableLiveData<NetworkResponse<SLRModel>>()
    val SLRresult : LiveData<NetworkResponse<SLRModel>> = _SLRresult

    fun getSLRAnswer(SLRrequest: SLRRequest) {
        _SLRresult.value = NetworkResponse.Loading
        viewModelScope.launch {
            try {
                val response = SLRapi.getSLRPara(SLRrequest.n, SLRrequest.x, SLRrequest.y, SLRrequest.alpha, SLRrequest.tail)
                if (response.isSuccessful) {
                    response.body()?.let {
                        Log.i("Response:", response.body().toString())
                        _SLRresult.value = NetworkResponse.Success(it)
                    }
                } else {
                    _SLRresult.value = NetworkResponse.Failure("Invalid Input")
                }
            }
            catch (e: Exception) {
                _SLRresult.value = NetworkResponse.Failure("Invalid Input")
            }
        }
    }
}