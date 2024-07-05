package com.example.kotlin_learning.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlin_learning.data.model.MultinomialModel
import com.example.kotlin_learning.data.request.MultinomialRequest
import com.example.kotlin_learning.data.response.NetworkResponse
import com.example.kotlin_learning.data.api.RetrofitInstance
import kotlinx.coroutines.launch

class MultinomialViewModel : ViewModel() {

    private val multinomialapi = RetrofitInstance.multinomialapi
    private val _multinomialresult = MutableLiveData<NetworkResponse<MultinomialModel>>()
    val multinomialresult : LiveData<NetworkResponse<MultinomialModel>> = _multinomialresult

    fun getMultinomialAnswer(multinomialrequest: MultinomialRequest) {
        _multinomialresult.value = NetworkResponse.Loading
        viewModelScope.launch {
            try {
                val response = multinomialapi.getMultinomialPara(multinomialrequest.n, multinomialrequest.x, multinomialrequest.p)
                if (response.isSuccessful) {
                    response.body()?.let {
                        Log.i("Response:", response.body().toString())
                        _multinomialresult.value = NetworkResponse.Success(it)
                    }
                } else {
                    _multinomialresult.value = NetworkResponse.Failure("Invalid Input")
                }
            }
            catch (e: Exception) {
                _multinomialresult.value = NetworkResponse.Failure("Invalid Input")
            }
        }
    }
}