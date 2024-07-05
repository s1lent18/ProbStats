package com.example.kotlin_learning.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlin_learning.data.model.AnovaModel
import com.example.kotlin_learning.data.request.AnovaRequest
import com.example.kotlin_learning.data.response.NetworkResponse
import com.example.kotlin_learning.data.api.RetrofitInstance
import kotlinx.coroutines.launch

class AnovaViewModel : ViewModel() {

    private val anovaapi = RetrofitInstance.anovaapi
    private val _anovaresult = MutableLiveData<NetworkResponse<AnovaModel>>()
    val anovaresult : LiveData<NetworkResponse<AnovaModel>> = _anovaresult

    fun getAnovaAnswer(anovaRequest: AnovaRequest) {
        _anovaresult.value = NetworkResponse.Loading
        viewModelScope.launch {
            try {
                val response = anovaapi.getAnovaPara(anovaRequest.n, anovaRequest.size, anovaRequest.sl)
                if (response.isSuccessful) {
                    response.body()?.let {
                        Log.i("Response:", response.body().toString())
                        _anovaresult.value = NetworkResponse.Success(it)
                    }
                } else {
                    _anovaresult.value = NetworkResponse.Failure("Invalid Input")
                }
            }
            catch (e: Exception) {
                _anovaresult.value = NetworkResponse.Failure("Invalid Input")
            }
        }
    }
}