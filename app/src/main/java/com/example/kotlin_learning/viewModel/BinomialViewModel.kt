package com.example.kotlin_learning.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlin_learning.data.model.BinomialModel
import com.example.kotlin_learning.data.request.BinomialRequest
import com.example.kotlin_learning.data.response.NetworkResponse
import com.example.kotlin_learning.data.api.RetrofitInstance
import kotlinx.coroutines.launch

class BinomialViewModel: ViewModel() {

    private val binomialapi = RetrofitInstance.binomialapi
    private val _binomialresult = MutableLiveData<NetworkResponse<BinomialModel>>()
    val binomialresult : LiveData<NetworkResponse<BinomialModel>> = _binomialresult

    fun getBinomialAnswer(binomialrequest: BinomialRequest) {
        _binomialresult.value = NetworkResponse.Loading
        viewModelScope.launch {
            try {
                val response = binomialapi.getBinomialPara(binomialrequest.n, binomialrequest.x, binomialrequest.p)
                if (response.isSuccessful) {
                    response.body()?.let {
                        Log.i("Response:", response.body().toString())
                        _binomialresult.value = NetworkResponse.Success(it)
                    }
                } else {
                    _binomialresult.value = NetworkResponse.Failure("Invalid Input")
                }
            }
            catch (e: Exception) {
                _binomialresult.value = NetworkResponse.Failure("Invalid Input")
            }
        }

    }
}